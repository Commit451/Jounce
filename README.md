# Jounce
Fancy debouncing

[![Build Status](https://travis-ci.org/Commit451/Jounce.svg?branch=master)](https://travis-ci.org/Commit451/Jounce)
[![](https://jitpack.io/v/Commit451/Jounce.svg)](https://jitpack.io/#Commit451/Jounce)

# Usage
See the sample project for a comprehensive example.
In a simple form, it looks a little something like this:
```java
long oneSecond = TimeUnit.SECONDS.toMillis(1);
//Void, since we are not keeping track of a value
final Debouncer<Void> debouncer = new Debouncer<Void>(oneSecond) {

    @Override
    public void onValueSet(Void value) {
        Snackbar.make(getWindow().getDecorView(), "Toolbar was clicked 1 second ago", Snackbar.LENGTH_SHORT)
                .show();
    }
};
toolbar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //starts/restarts the deboucing
        debouncer.setValue(null);
    }
});
```
The sample shows usage within a RecyclerView, as well as usage for search ahead within an example search activity

License
--------

    Copyright 2017 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
