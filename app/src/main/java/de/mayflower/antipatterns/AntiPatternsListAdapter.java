package de.mayflower.antipatterns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Comparator;

import de.mayflower.antipatterns.data.Pattern;
import de.mayflower.antipatterns.data.PatternComparator;

public class AntiPatternsListAdapter extends ArrayAdapter<Pattern>
{
    private Pattern[]     patterns;
    private final Context context;

    public AntiPatternsListAdapter(Context context, Pattern[] patterns)
    {
        super(context, R.layout.antipatterns_list_item, patterns);
        this.patterns = patterns;
        this.context  = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.antipatterns_list_item, parent, false);

        TextView itemTitle = (TextView) view.findViewById(R.id.text_item_title);
        itemTitle.setText(getItem(position).getNameWithCounter());

        return view;
    }

    public void sort()
    {
        PatternComparator comparator = new PatternComparator();
        super.sort(comparator);
    }
}
