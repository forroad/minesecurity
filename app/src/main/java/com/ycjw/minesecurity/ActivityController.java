package com.ycjw.minesecurity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityController {
    private static List<Activity> activities = new ArrayList<>();

    public static void addActiovity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }

    public static Activity getLastActivity(){
        return activities.get(activities.size() - 1);
    }
}
