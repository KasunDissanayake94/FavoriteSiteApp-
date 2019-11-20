package com.example.favoritesiteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UrlSiteAdapter mFavAdapter;
    private UrlViewModel mUrlViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fab);

        mUrlViewModel = ViewModelProviders.of(this).get(UrlViewModel.class);

        List<UrlSites> urlSites = mUrlViewModel.getFavs();
        mFavAdapter = new UrlSiteAdapter(this, R.layout.list_item_row, urlSites);
        listView.setAdapter(mFavAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inUrl = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New favourite")
                        .setMessage("Add a url link below")
                        .setView(inUrl)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = String.valueOf(inUrl.getText());
                                long date = (new Date()).getTime();
                                // VM AND VIEW
                                mFavAdapter.add(mUrlViewModel.addFav(url, date));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
    }

    public void deleteFav(View view) {
        View parent = (View) view.getParent();
        int position = (int) parent.getTag(R.id.POS);
        UrlSites urlSites = mFavAdapter.getItem(position);
        // VM
        mUrlViewModel.removeFav(urlSites.mId);
        // VIEW
        mFavAdapter.remove(urlSites);
    }

    public class UrlSiteAdapter extends ArrayAdapter<UrlSites> {

        private class ViewHolder {
            TextView tvUrl;
            TextView tvDate;
        }

        public UrlSiteAdapter(Context context, int layoutResourceId, List<UrlSites> todos) {
            super(context, layoutResourceId, todos);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            UrlSites favourites = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_item_row, parent, false);
                viewHolder.tvUrl = convertView.findViewById(R.id.tvUrl);
                viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
                convertView.setTag(R.id.VH, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(R.id.VH);

            }

            viewHolder.tvUrl.setText(favourites.mUrl);
            viewHolder.tvDate.setText((new Date(favourites.mDate).toString()));
            convertView.setTag(R.id.POS, position);
            return convertView;
        }



    }
}
