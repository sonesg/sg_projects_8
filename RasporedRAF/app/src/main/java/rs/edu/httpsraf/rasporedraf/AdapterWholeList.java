package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterWholeList extends ArrayAdapter<String> {

    AdapterWholeList(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        String newString = getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.item_whole_list, null);
        }

        TextView name = view.findViewById(R.id.text_whole_list);

        if (name != null)
            name.setText(newString);
        return view;
    }
}
