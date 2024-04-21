package br.app.gym_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.app.gym_app.view.R;

public class SharedPreferencesManager {

    private Context context;
    private SharedPreferences preferences;
    private  SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void clearPreferences(){
        editor.clear();
        editor.apply();
    }

    public SharedPreferences.Editor getEditor(){
        return editor;
    }
}
