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

public class AdapterListSchedule extends ArrayAdapter<String> {


    AdapterListSchedule(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        String newString;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.itemlist, null);
        }

        String nameString = getItem(position);

        if (nameString != null) {

            switch (nameString) {
                case "Ucionica 1":
                    nameString = "U1";
                    break;
                case "Ucionica 3":
                    nameString = "U3";
                    break;
                case "Ucionica 4":
                    nameString = "U4";
                    break;
                case "Ucionica 5":
                    nameString = "U5";
                    break;
                case "Ucionica 6":
                    nameString = "U6";
                    break;
                case "Ucionica 7":
                    nameString = "U7";
                    break;
                case "Ucionica 8":
                    nameString = "U8";
                    break;
                case "Ucionica 9":
                    nameString = "U9";
                    break;
                case "Profesorski kabinet":
                    nameString = "P.K.";
                    break;
            }

            if (nameString.length() > 5) {
                newString = nameString.substring(0, 5);
            } else {
                newString = nameString;
            }

            TextView name = view.findViewById(R.id.name_item_list);

            if (name != null)
                name.setText(newString);
        }

        return view;
    }

}