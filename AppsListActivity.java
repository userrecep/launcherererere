package launcher.example.recepdagli.simplelauncher;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ridvangursoy on 27.10.2016.
 */

public class AppsListActivity extends Activity {

    private PackageManager manager;
    private List<AppInfo> apps;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        loadApps();
        loadListView();
        addClickListener();
    }

    private void addClickListener()
    {
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                AppsListActivity.this.startActivity(i);
            }
        });
    }

    private void loadListView()
    {
        list = (ListView)findViewById(R.id.apps_list);

        ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>(this, R.layout.list_item, apps)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolderItem viewHolder = null;

                if (convertView ==  null)
                {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                    viewHolder = new ViewHolderItem();
                    viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
                    viewHolder.label = (TextView)convertView.findViewById(R.id.label);
                    viewHolder.name = (TextView)convertView.findViewById(R.id.name);

                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder = (ViewHolderItem) convertView.getTag();
                }
                AppInfo apinfo = apps.get(position);
                if(apinfo != null) {
                    viewHolder.icon.setImageDrawable(apinfo.icon);
                    viewHolder.label.setText(apinfo.label);
                    viewHolder.name.setText(apinfo.name);

                }
                return convertView;

            }
            final class ViewHolderItem
            {
                ImageView icon;
                TextView label;
                TextView name;
            }
        };
        list.setAdapter(adapter);
    }

    private void loadApps()
    {
        manager = getPackageManager();
        apps = new ArrayList<AppInfo>();
        Intent i = new Intent(Intent.ACTION_MAIN,null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);


        List<ResolveInfo> availableActivites = manager.queryIntentActivities(i,0);
        for (ResolveInfo ri : availableActivites) {
            AppInfo apinfo = new AppInfo();
            apinfo.label = ri.loadLabel(manager);
            apinfo.name = ri.activityInfo.packageName;
            apinfo.icon = ri.activityInfo.loadIcon(manager);
            apps.add(apinfo);
        }

    }
}
