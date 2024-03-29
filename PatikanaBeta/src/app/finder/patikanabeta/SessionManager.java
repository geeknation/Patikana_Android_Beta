package app.finder.patikanabeta;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "";
	
	// User id (make variable public to access from outside)
	public static final String KEY_uID = "";
	
	// User name (make variable public to access from outside)
	public static final String KEY_uNAME = "";
	
	public static final String PREF_NAME = "AGENT";
	
	
	// Context
	Context _context;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Shared Preferences
	SharedPreferences pref;

	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			_context.startActivity(i);
		}
		
	}	
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String username, String userid){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_uNAME, username);
		
		// Storing email in pref
		editor.putString(KEY_uID, userid);
		
		// commit changes
		editor.commit();
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_uNAME, pref.getString(KEY_uNAME, null));
		
		// user id
		user.put(KEY_uID, pref.getString(KEY_uID, null));
		
		// return user
		return user;
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
}
