package com.commit451.jounce.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.jounce.Debouncer;
import com.commit451.teleprinter.Teleprinter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Show search example
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search)
    EditText mSearchView;
    @BindView(R.id.clear)
    View mClearView;

    Teleprinter mTeleprinter;

    private final Debouncer<CharSequence> mSearchDebouncer = new Debouncer<CharSequence>() {
        @Override
        public void onValueSet(CharSequence value) {
            search(value);
        }
    };

    private final TextView.OnEditorActionListener mOnSearchEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (TextUtils.isEmpty(mSearchView.getText())) {
                mSearchView.setText("unicorns");
            }
            mSearchDebouncer.cancel();
            String query = mSearchView.getText().toString();
            search(query);
            mTeleprinter.hideKeyboard();
            return false;
        }
    };

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                mClearView.animate().alpha(0.0f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mClearView.setVisibility(View.GONE);
                    }
                });
            } else if (count == 1 && mClearView.getVisibility() != View.VISIBLE) {
                mClearView.setVisibility(View.VISIBLE);
                mClearView.animate().alpha(1.0f);
            }
            if (s != null &&  s.length() > 3) {
                mSearchDebouncer.setValue(s);
            }
            //This means they are backspacing
            if (before > count) {
                mSearchDebouncer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @OnClick(R.id.clear)
    void onClearClick() {
        mClearView.animate().alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                mClearView.setVisibility(View.GONE);
            }
        });
        mSearchView.getText().clear();
        mTeleprinter.showKeyboard(mSearchView);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTeleprinter = new Teleprinter(this);
        mSearchView.addTextChangedListener(mTextWatcher);
        mSearchView.setOnEditorActionListener(mOnSearchEditorActionListener);
    }

    private void search(CharSequence query) {
        Toast.makeText(SearchActivity.this, "Searching for " + query, Toast.LENGTH_SHORT).show();
    }
}
