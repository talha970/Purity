package fyp.teejay.apollo.fyp1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.HashMap;

/**
 * {@link RecyclerView.Adapter} that can display a {@link //DummyItem} and makes a call to the
 * specified {@link MarkerFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMarkerRecyclerViewAdapter extends RecyclerView.Adapter<MyMarkerRecyclerViewAdapter.ViewHolder> {

    private final HashMap<String,Markers> mValues;
    private final MarkerFragment.OnListFragmentInteractionListener mListener;
private Intent intent;

    public MyMarkerRecyclerViewAdapter(HashMap<String,Markers> items, MarkerFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(String.valueOf(position));
            holder.mName.setText(mValues.get(String.valueOf(position)).getName());
            holder.mAddress.setText(mValues.get(String.valueOf(position)).getAddress());
        holder.mCity.setText(mValues.get(String.valueOf(position)).getCity());
        holder.cd.setTextSize(10.0f);
       holder.mDist.setText((int) mValues.get(String.valueOf(position)).getdist()+" Km away");
if(mValues.get(String.valueOf(position)).getPurity()!=null) {
    holder.cd.showValue(Float.parseFloat(mValues.get(String.valueOf(position)).getPurity()), 100f, true);
}
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        intent = new Intent(v.getContext(), MapsActivity.class);
                        intent.putExtra("Markerobj", holder.mItem);
                        Log.d("onclick", holder.mItem.getName());
                        mListener.onListFragmentInteraction(holder.mItem);
                        v.getContext().startActivity(intent);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mAddress;
        public final TextView mDist;
        public final TextView mCity;
        public CircleDisplay cd;
        public Markers mItem;
        public Context con;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.tv_name);
            mAddress = (TextView) view.findViewById(R.id.tv_add);
            mCity = (TextView) view.findViewById(R.id.tv_city);
            mDist = (TextView) view.findViewById(R.id.tv_dist);
            cd = (CircleDisplay) view.findViewById(R.id.circleDisplay);
            con=view.getContext();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddress.getText() + "'";
        }
    }
}
