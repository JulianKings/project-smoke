/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.listviewanimations.util;



import android.view.View;
import android.widget.ListAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class StickyListHeadersListViewWrapper implements ListViewWrapper {


    private final StickyListHeadersListView mListView;

    public StickyListHeadersListViewWrapper(final StickyListHeadersListView listView) {
        mListView = listView;
    }


    @Override
    public StickyListHeadersListView getListView() {
        return mListView;
    }


    @Override
    public View getChildAt(final int index) {
        return mListView.getListChildAt(index);
    }

    @Override
    public int getFirstVisiblePosition() {
        return mListView.getFirstVisiblePosition();
    }

    @Override
    public int getLastVisiblePosition() {
        return mListView.getLastVisiblePosition();
    }

    @Override
    public int getCount() {
        return mListView.getCount();
    }

    @Override
    public int getChildCount() {
        return mListView.getChildCount();
    }

    @Override
    public int getHeaderViewsCount() {
        return mListView.getHeaderViewsCount();
    }

    @Override
    public int getPositionForView(final View view) {
        return mListView.getPositionForView(view);
    }


    @Override
    public ListAdapter getAdapter() {
        return mListView.getAdapter();
    }

    @Override
    public void smoothScrollBy(final int distance, final int duration) {
        mListView.smoothScrollBy(distance, duration);
    }
}
