package com.commit451.jounce.sample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.commit451.jounce.Debouncer;
import com.commit451.teleprinter.Teleprinter;

/**
 * Show search example
 */
public class SearchActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mSearchView;
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
            mTeleprinter.hideKeyboard(0);
            return false;
        }
    };

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

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
            if (s != null && s.length() > 3) {
                mSearchDebouncer.setValue(s);
            }
            //This means they are backspacing
            if (before > count) {
                mSearchDebouncer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mToolbar = findViewById(R.id.toolbar);
        mSearchView = findViewById(R.id.search);
        mClearView = findViewById(R.id.clear);
        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchDebouncer.cancel();
                mClearView.animate().alpha(0.0f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mClearView.setVisibility(View.GONE);
                    }
                });
                mSearchView.getText().clear();
                mTeleprinter.showKeyboard(mSearchView, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTeleprinter = new Teleprinter(this, false);
        mSearchView.addTextChangedListener(mTextWatcher);
        mSearchView.setOnEditorActionListener(mOnSearchEditorActionListener);
    }

    private void search(CharSequence query) {
        Toast.makeText(SearchActivity.this, "Searching for " + query, Toast.LENGTH_SHORT).show();
    }
}
