package com.mcma.modules.contacts.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SectionIndexer;

import com.mcma.app.constants.AppConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by anil on 2/8/2017.
 */

public abstract class SectionCursorRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements SectionIndexer {

    public static final int NO_CURSOR_POSITION = -99;
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_SECTION = 1;
    private Object mPresenter;
    private Cursor currentCursor;
    private boolean isDataValid;
    private final DataSetObserver dataSetObserver;
    protected SortedMap<Integer, Object> Sections = new TreeMap<Integer, Object>();
    protected ArrayList<Integer> sectionList = new ArrayList<Integer>();
    private Object[] fastScrollObjects;
    private boolean isFavotiteAdapter = false;

    public SectionCursorRecyclerViewAdapter(Context context, Cursor cursor, Object presenter,boolean isFavotiteAdapter) {
        currentCursor = cursor;
        this.isFavotiteAdapter = isFavotiteAdapter;
        isDataValid = (cursor != null);
        dataSetObserver = new NotifyingDataSetObserver();
        if (currentCursor != null)
            currentCursor.registerDataSetObserver(dataSetObserver);
    }

    public Cursor getCursor() {
        return currentCursor;
    }

    protected boolean hasOpenCursor() {
        final Cursor cursor = getCursor();
        if (cursor == null || cursor.isClosed()) {
            swapCursor(null);
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return (isDataValid && currentCursor != null) ? currentCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (isSection(position))
            return position;
        else {
            final int cursorPosition = getCursorPositionWithoutSections(position);
            final Cursor cursor = getCursor();
            if (hasOpenCursor() && cursor.moveToPosition(cursorPosition))
                return cursor.getLong(cursor.getColumnIndex("_id"));
            else
                return NO_CURSOR_POSITION;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isSection(position) ? VIEW_TYPE_SECTION : VIEW_TYPE_NORMAL;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(T viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(T viewHolder, int position) {
        Log.i(AppConstants.APP_TAG, "onBindViewHolder :" + position);
        if (!isDataValid)
            throw new IllegalStateException("invalid cursor");
        if (!currentCursor.moveToPosition(position))
            throw new IllegalStateException("couldn't move cursor to position " + position);
        onBindViewHolder(viewHolder, currentCursor);
    }

    public void changeCursor(Cursor cursor) {
        final Cursor old = swapCursor(cursor);
        if (old != null)
            old.close();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == currentCursor)
            return null;
        final Cursor oldCursor = currentCursor;
        if (oldCursor != null && dataSetObserver != null)
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        currentCursor = newCursor;
        if (currentCursor != null) {
            if (dataSetObserver != null)
                currentCursor.registerDataSetObserver(dataSetObserver);
            isDataValid = true;
            buildSections(currentCursor);
            notifyDataSetChanged();
        } else {
            isDataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    protected abstract Object getSectionFromCursor(Cursor cursor);

    protected void buildSections() {
        if (hasOpenCursor()) {
            final Cursor cursor = getCursor();
            cursor.moveToPosition(-1);
            buildSections(cursor);
            if (Sections == null)
                Sections = new TreeMap<Integer, Object>();
        }
    }

    protected void buildSections(Cursor cursor) {
        Sections.clear();
        int cursorPosition = 0;
        if(isFavotiteAdapter){
            final Object sectionObject = null;
            Sections.put(cursorPosition, sectionObject);
        }else{
            while (hasOpenCursor() && cursor.moveToNext()) {
                final Object sectionObject = getSectionFromCursor(cursor);
                if (cursor.getPosition() != cursorPosition)
                    throw new IllegalStateException("cursor moved");
                if (!Sections.containsValue(sectionObject)) {
                    Sections.put(cursorPosition, sectionObject);
                }
                cursorPosition++;
            }
        }
    }

    public boolean isSection(int position) {
        return Sections.containsKey(position);
    }

    public int getCursorPositionWithoutSections(int position) {
        if (Sections.size() == 0)
            return position;
        else if (!isSection(position)) {
            final int sectionIndex = getIndexWithinSections(position);
            if (isListPositionBeforeFirstSection(position, sectionIndex))
                return position;
            else
                return position - (sectionIndex + 1);
        } else
            return NO_CURSOR_POSITION;
    }

    public int getIndexWithinSections(int position) {
        boolean isSection = false;
        int numPrecedingSections = 0;
        for (final Integer sectionPosition : Sections.keySet())
            if (position > sectionPosition)
                numPrecedingSections++;
            else if (position == sectionPosition)
                isSection = true;
            else
                break;
        return isSection ? numPrecedingSections : Math.max(numPrecedingSections - 1, 0);
    }

    private boolean isListPositionBeforeFirstSection(int position, int sectionIndex) {
        final boolean hasSections = (Sections != null && Sections.size() > 0);
        return (sectionIndex == 0 && hasSections && position < Sections.firstKey());
    }

    public Object getSection(int position) {
        return Sections.get(position);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionList.size() == 0)
            for (final Integer key : Sections.keySet())
                sectionList.add(key);
        return (sectionIndex < sectionList.size()) ? sectionList.get(sectionIndex) : getItemCount();
    }

    @Override
    public int getSectionForPosition(int position) {
        final Object[] objects = getSections();
        final int sectionIndex = getIndexWithinSections(position);
        return sectionIndex < objects.length ? sectionIndex : 0;
    }

    @Override
    public Object[] getSections() {
        if (fastScrollObjects == null)
            fastScrollObjects = getFastScrollDialogLabels();
        return fastScrollObjects;
    }

    protected int getMaxIndexerLength() {
        return 3;
    }

    private Object[] getFastScrollDialogLabels() {
        final Collection<Object> sectionsCollection = Sections.values();
        final Object[] objects = sectionsCollection.toArray(new Object[sectionsCollection.size()]);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            final int max = getMaxIndexerLength();
            for (int i = 0; i < objects.length; i++)
                if (objects[i].toString().length() >= max)
                    objects[i] = objects[i].toString().substring(0, max);
        }
        return objects;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            isDataValid = true;
            buildSections();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            isDataValid = false;
            buildSections();
            notifyDataSetChanged();
        }
    }

    protected abstract Object getObjForPosition(Cursor cursor);

    protected abstract int getLayoutIdForPosition(int position);
}
