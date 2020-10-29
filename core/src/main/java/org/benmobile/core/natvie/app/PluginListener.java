/*
 * Syknet Project
 * Copyright (c) 2016 chunquedong
 * Licensed under the LGPL(http://www.gnu.org/licenses/lgpl.txt), Version 3
 */
package org.benmobile.core.natvie.app;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;
import android.widget.Toast;

import org.benmobile.core.natvie.update.service.UpdateService;

public class PluginListener {
	protected Context context;
	protected ProgressDialog progressDialog = null;
	protected boolean finishLaunchActivity;

	public PluginListener(Context ctx) {
		context = ctx;
	}

	public void onError(String id, String error) {
		showMessage("Download Plugin Error, id:" + id + ", error:" + error);
	}

	public String getApplicationName() {
		try {
			PackageManager packageManager = null;
			ApplicationInfo applicationInfo = null;
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);

			String applicationName = (String) packageManager
					.getApplicationLabel(applicationInfo);
			return applicationName;
		} catch (Exception e) {
		}
		return "";
	}

	public void onUpdate(String id, String whatsNew) {
		String title = getApplicationName() + "The updated, need to restart the application";
		String msg = whatsNew + "\n(Powered by Syknet Plug-in system)";

		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(title).setMessage(msg).setCancelable(false)
				.setPositiveButton("determine", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						restartApp();
					}
				});
		if (PluginClient.config.forceRestart == false) {
			builder.setNegativeButton("cancel", null);
		}
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
	}

	public void onLoaded(String id) {
		PluginClient.log.d("onLoaded");
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void onProgress(String id, float percent, int max, int type) {
		if (progressDialog == null && type == UpdateService.installType_launch) {
			Context act = PluginClient.getInstance().tempContext;
			if (act != null && act instanceof Activity) {
				progressDialog = new ProgressDialog(act);
				progressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setIndeterminate(false);
				progressDialog.setTitle("Downloading");
				progressDialog.setMessage(" Downloading, please wait!");
				progressDialog.setMax(max);
				progressDialog.show();
			}
		}

		if (progressDialog != null) {
			progressDialog.setProgress((int) (percent * max));
		}
		PluginClient.log.d("onProgress " + percent);
	}

	public void onLaunch(String id) {
		if (finishLaunchActivity) {
			Context act = PluginClient.getInstance().tempContext;
			if (act != null && act instanceof Activity) {
				((Activity) act).finish();
			}
		}
	}

	public void showMessage(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void restartApp() {
		PluginClient.log.i("will restart");

		 Intent i = context.getPackageManager().getLaunchIntentForPackage(
		 context.getPackageName());
		 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		 int pendingId = 1;
		 PendingIntent pendingIntent = PendingIntent.getActivity(context,
		 pendingId, i, PendingIntent.FLAG_CANCEL_CURRENT);
		 AlarmManager mgr = (AlarmManager) context
		 .getSystemService(Context.ALARM_SERVICE);
		 mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
		 pendingIntent);

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.killBackgroundProcesses(context.getPackageName());
		System.exit(0);
	}
}
