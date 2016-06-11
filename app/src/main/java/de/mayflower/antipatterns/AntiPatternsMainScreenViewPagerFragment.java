package de.mayflower.antipatterns;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.mayflower.antipatterns.data.Category;

public class AntiPatternsMainScreenViewPagerFragment extends ListFragment
{
    private Category                      category;
    private ListFragmentCallbackInterface callbacks = dummyCallbacks;

    /**
     * A dummy implementation of the {@link ListFragmentCallbackInterface} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static ListFragmentCallbackInterface dummyCallbacks = new ListFragmentCallbackInterface() {
        @Override
        public void onItemSelected(Integer patternId) {
        }
    };

    public void init(Category category)
    {
        this.category = category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AntiPatternsListAdapter listAdapter = new AntiPatternsListAdapter(
                getActivity(),
                AntiPatternsHydrator.getPatternsForCategory(category.getId())
        );

        setListAdapter(listAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ich = super.onCreateView(inflater, container, savedInstanceState);
        ListView lv = (ListView) ich.findViewById(android.R.id.list);
        lv.setDivider(new ColorDrawable(getResources().getColor(R.color.separator_line)));
        lv.setDividerHeight(0);

        return ich;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ListFragmentCallbackInterface)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (ListFragmentCallbackInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        callbacks = dummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        callbacks.onItemSelected(category.getPatterns()[position]);
    }

    public String getTitle()
    {
        return category.getName();
    }

    @Override
    public void onResume() {
        super.onResume();

        AntiPatternsListAdapter listAdapter = (AntiPatternsListAdapter) getListAdapter();

        if (category != null && category.getId() == AntiPatternsHydrator.TOP10_CATEGORY) {
            AntiPatternsHydrator.generateTop10((Context) getActivity());
            listAdapter.sort();
        }

        listAdapter.notifyDataSetChanged();
    }
}
