package fyp.teejay.apollo.fyp1;

/**
 * Created by teejay on 3/28/2016.
 */
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReadingAdapter extends ArrayAdapter<Readings> {

    // declaring our ArrayList of items
    private ArrayList<Readings> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public ReadingAdapter(Context context, int textViewResourceId, ArrayList<Readings> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.readings_list, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Readings i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView ph = (TextView) v.findViewById(R.id.pH);
            TextView cond = (TextView) v.findViewById(R.id.conduc);
            TextView turb = (TextView) v.findViewById(R.id.turb);
            TextView date = (TextView) v.findViewById(R.id.date);


            // check to see if each individual textview is null.
            // if not, assign some text!

            if (ph != null){
                ph.setText(i.get_ph());
            }
            if (cond != null){
                cond.setText(i.get_cond());
            }
            if (turb != null){
                turb.setText(i.get_turbudity());
            }
            if (date!=null){
                date.setText(i.get_date().substring(0,10));
            }
        }

        // the view must be returned to our activity
        return v;

    }

}