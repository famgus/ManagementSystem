package com.ec.managementsystem.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.PedidoDetail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

@SuppressLint("Wakelock")
@SuppressWarnings("deprecation")
public class Utils {


    public static void SaveKey(String key, String value) {
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            SharedPreferences.Editor edit = example.edit();
            edit.putString(key, value);
            edit.commit();
        } catch (Exception e) {
            CreateLogFile("Utils.SaveKey: " + e.getMessage());
        }
    }

    public static String GetValue(String key) {
        String value = "";
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            value = example.getString(key, "");
        } catch (Exception e) {
            CreateLogFile("Utils.GetValue: key: " + key + " error: " + e.getMessage());
        }
        return value;
    }

    public static void PlayBeep() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MyApplication.GetAppContext(), notification);
            r.play();
        } catch (Exception e) {
            CreateLogFile("Utils.PlayBeep: " + e.getMessage());
        }
    }

    public static void PlaySound(boolean loop) {
        try {
            StopSound();
            MyApplication.setMediaPlayer(MediaPlayer.create(MyApplication.GetAppContext(), R.raw.beep));
            MyApplication.getMediaPlayer().setLooping(loop);
            MyApplication.getMediaPlayer().start(); // no need to call prepare(); create() does that for you
        } catch (Exception e) {
            CreateLogFile("Utils.PlaySound: " + e.getMessage());
        }
    }

    public static void StopSound() {
        try {
            if (MyApplication.getMediaPlayer() != null) {
                MyApplication.getMediaPlayer().release();
            }
        } catch (Exception e) {
            CreateLogFile("Utils.StopSound: " + e.getMessage());
        }
    }

    public static void ShowDialog(Activity actv, String subject, String body) {
        try {
            PlayBeep();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(actv);
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(body)
                    .setPositiveButton(actv.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            if (subject != null && subject.length() > 0) {
                alertDialogBuilder.setTitle(subject);
            }

            try {
                //enable audio
                AudioManager mgr = (AudioManager) actv.getSystemService(Context.AUDIO_SERVICE);
                mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //Wake up screen
                PowerManager powerManager = (PowerManager) actv.getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } catch (Exception e) {
                Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
            }

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
        }
    }

    public static void ShowSystemAlert2(String subject, String body, int expirationTime, int action) {
        try {
            //enable audio
            /*try {
                AudioManager mgr = (AudioManager) MyApplication.GetAppContext().getSystemService(Context.AUDIO_SERVICE);
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);

                //Wake up screen
                PowerManager powerManager = (PowerManager) MyApplication.GetAppContext().getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } catch (Exception e) {
            }

            Intent i = new Intent(MyApplication.GetAppContext(), AlertDialogClass.class);
            i.putExtra("action", action);
            i.putExtra("subject", subject);
            i.putExtra("body", body);
            i.putExtra("driver", driver);
            i.putExtra("coDriver", coDriver);
            i.putExtra("expirationTime", expirationTime);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            MyApplication.GetAppContext().startActivity(i);*/
        } catch (Exception e) {
            CreateLogFile("Utils.ShowSystemAlert: " + e.getMessage());
        }
    }


    public static void ShowSystemAlert(String subject, String body, int expirationTime, int action) {
        try {
            //enable audio
            try {
                AudioManager mgr = (AudioManager) MyApplication.GetAppContext().getSystemService(Context.AUDIO_SERVICE);
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);

                //Wake up screen
                PowerManager powerManager = (PowerManager) MyApplication.GetAppContext().getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } catch (Exception e) {
            }

            Notification notification = createNotification(action, subject, body, expirationTime);
            showNotification(notification);
        } catch (Exception e) {
            CreateLogFile("Utils.ShowSystemAlert: " + e.getMessage());
        }
    }


    private static Notification createNotification(int action, String subject, String body, int expirationTime) {
        /*Context context = MyApplication.GetAppContext();
        String ACTION_LOCK_DRIVER = "ACTION_LOCK_DRIVER";
        String ACTION_LOCK_CODRIVER = "ACTION_LOCK_CODRIVER";
        String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";
        Intent deleteIntent = new Intent(context, NotificationReceiver.class);
        deleteIntent.setAction(ACTION_DELETE_NOTIFICATION);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

        String CHANNEL_ID = context.getString(R.string.default_notification_channel_id);//"channel_02";
        NotificationCompat.Builder builder;
        Uri beepSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSound = Uri.parse("android.resource://" + MyApplication.GetAppContext().getPackageName() + "/" + R.raw.silence);
        if (action == Core.SystemAlertMotive.RESERVED.ordinal() && CanPlaySound()) {
            defaultSound = beepSound;
        } else if (action == Core.SystemAlertMotive.EXEMPTION_DRIVER.ordinal() && CanPlaySound()) {
            defaultSound = beepSound;
        } else if (action == Core.SystemAlertMotive.UNIDENTIFIED_MOVEMENT.ordinal() && CanPlaySound()) {
            PlaySound();
        } else if (action == Core.SystemAlertMotive.MOVEMENT.ordinal()) {
            PlaySound();
            Intent driverIntent = new Intent(context, NotificationReceiver.class);
            driverIntent.setAction(ACTION_LOCK_DRIVER);

            Intent coDriverIntent = new Intent(context, NotificationReceiver.class);
            coDriverIntent.setAction(ACTION_LOCK_CODRIVER);
            PendingIntent driverPendingIntent =
                    PendingIntent.getBroadcast(context, 0, driverIntent, 0);
            PendingIntent coDriverPendingIntent =
                    PendingIntent.getBroadcast(context, 0, coDriverIntent, 0);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(context.getString(R.string.select_driver_driving))
                    .setContentTitle(context.getString(R.string.warning))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.app_icon)
                    .setTicker(context.getString(R.string.app_name))
                    .addAction(R.drawable.ic_drive_eta_pink_32dp, driver.getHosUserName(), driverPendingIntent)
                    .addAction(R.drawable.ic_drive_eta_pink_32dp, coDriver.getHosUserName(), coDriverPendingIntent)
                    .setTimeoutAfter(expirationTime * 1000)
                    .setDeleteIntent(deletePendingIntent)
                    .setContentIntent(deletePendingIntent)
                    .setSound(defaultSound)
                    .setOnlyAlertOnce(true)
                    .setWhen(System.currentTimeMillis());
            return builder.build();
        }
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText(body)
                .setContentTitle(subject)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.app_icon)
                .setTicker(context.getString(R.string.app_name))
                .setTimeoutAfter(expirationTime * 1000)
                .setDeleteIntent(deletePendingIntent)
                .setContentIntent(deletePendingIntent)
                .setSound(defaultSound)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis());
        return builder.build();*/
        return null;
    }


    private static void showNotification(Notification notification) {
        Context context = MyApplication.GetAppContext();
        NotificationManager mNotificationManager;
        String CHANNEL_ID = context.getString(R.string.default_notification_channel_id);
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            // Android O requires a Notification Channel.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.app_name);
                // Create the channel for the notification
                NotificationChannel mChannel =
                        new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);

                mChannel.setShowBadge(false);
                // Set the Notification Channel for the Notification Manager.
                mNotificationManager.createNotificationChannel(mChannel);
            }
            mNotificationManager.notify(Core.LOCK_DRIVER_NOTIFICATION_ID, notification);
        }
    }


    private static boolean isExternalStorageReadOnly() {
        try {
            String extStorageState = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
        } catch (Exception e) {
            CreateLogFile("Utils.isExternalStorageReadOnly: " + e.getMessage());
            return false;
        }
    }

    private static boolean isExternalStorageAvailable() {
        try {
            String extStorageState = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(extStorageState);
        } catch (Exception e) {
            CreateLogFile("Utils.isExternalStorageAvailable: " + e.getMessage());
            return false;
        }
    }


    public static boolean IsGPSProviderEnabled() {
        try {
            LocationManager locManager = (LocationManager) MyApplication.GetAppContext().getSystemService(Context.LOCATION_SERVICE);
            return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            CreateLogFile("Utils.IsGPSProviderEnabled: " + e.getMessage());
            return false;
        }
    }

    public static int BatteryLevel() {
        int result = 0;
        try {
            Intent intent = MyApplication.GetAppContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (intent != null) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                result = (level * 100) / scale;
                return result;
            }
        } catch (Exception e) {
            CreateLogFile("Utils.BatteryLevel: " + e.getMessage());
            return result;
        }
        return result;
    }

    public static long StorageAvailable() {
        long size = 0;
        try {
            size = Environment.getDataDirectory().getFreeSpace();
            return size;
        } catch (Exception e) {
            CreateLogFile("Utils.BatteryLevel: " + e.getMessage());
            return size;
        }
    }

    public static void CreateLogFile(String message) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) return;

        File folder = new File(Core.STORAGE_PATH);
        if (!folder.exists()) folder.mkdir();

        final String filename = folder.toString() + "/" + "HOSLogs.csv";
        File file = new File(filename);
        file.setReadOnly();

        FileWriter fw;
        try {
            fw = new FileWriter(filename, true);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            fw.append("GMT: " + DateFormat.format(date) + " Msg: " + message);
            fw.append("\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    public static void CreateECMLogFile(String message) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) return;

        File folder = new File(Core.STORAGE_PATH);
        if (!folder.exists()) folder.mkdir();
        final String filename = folder.toString() + "/" + "HOSECMLogs.csv";
        File file = new File(filename);
        file.setReadOnly();

        FileWriter fw;
        try {
            fw = new FileWriter(filename, true);
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            fw.append("GMT: " + DateFormat.format(date) + " Msg: " + message);
            fw.append("\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    public static void DeleteECMLogFile() {
        if (isExternalStorageReadOnly()) return;

        File folder = new File(Core.STORAGE_PATH);
        if (!folder.exists()) folder.mkdir();

        try {
            final String filename = folder.toString() + "/" + "HOSECMLogs.csv";
            File file = new File(filename);
            file.delete();
        } catch (Exception e) {
        }
    }

    public static String FormatTimeToString(double hours) {
        String result = "00h 00m";
        try {
            int wholePart = (int) hours;
            double fractionalPart = hours - wholePart;
            fractionalPart = fractionalPart * 60;

            String hoursString = String.format("%02d", wholePart);
            String minutesString = String.format("%02d", (int) fractionalPart);

            result = String.format("%sh %sm", hoursString, minutesString);
        } catch (Exception e) {
        }

        return result;
    }

    public static String FormatDateToString(long startDate, long endDate, String hoursAbb, String minutesAbb) {
        String result = "00" + hoursAbb + " 00" + minutesAbb;
        try {
            long diff = Math.abs(startDate - endDate);
            int hours = (int) (diff / 3600);
            result = String.valueOf(hours) + hoursAbb + String.valueOf((diff / 60) - (hours * 60)) + minutesAbb;
            return result;
        } catch (Exception e) {
            CreateLogFile("Utils.FormatDateToString: " + e.getMessage());
            return result;
        }
    }

    public static String FormatSecondToString(long timeInSeconds) {
        int hours = 0;
        int minutes = 0;
        try {
            if (timeInSeconds > 0) {
                hours = (int) (timeInSeconds / 3600);
                minutes = (int) ((timeInSeconds / 60) - (hours * 60));
            }
        } catch (Exception e) {
            CreateLogFile("Utils.FormatSecondToString: " + e.getMessage());
        }
        return String.format("%02d:%02d", hours, minutes);
    }

    public static boolean IsOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) MyApplication.GetAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            CreateLogFile("Utils.IsOnline: " + e.getMessage());
            return false;
        }
    }

    public static boolean IsWifiConnected() {
        try {
            ConnectivityManager connManager = (ConnectivityManager) MyApplication.GetAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) {
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi != null) return mWifi.isConnected();
            }
            return false;
        } catch (Exception e) {
            CreateLogFile("Utils.IsWifiConnected: " + e.getMessage());
            return false;
        }
    }

    private static int ConvertStringToDecimal(String string) {
        int value = 0;
        try {
            for (int i = 0; i < string.length(); i++) {
                char letter = string.charAt(i);
                if (Character.isLetter(letter) || Character.isDigit(letter)) {
                    int temp = (int) letter;
                    value += temp - 48;
                }
            }
        } catch (Exception e) {
            CreateLogFile("Utils.ConvertStringToDecimal: " + e.getMessage());
        }
        return value;
    }

    private static String GetNumericDigits(String value) {
        String finalValue = "";
        try {
            for (int i = 0; i < value.length(); i++) {
                char letter = value.charAt(i);
                if (Character.isLetter(letter) || Character.isDigit(letter)) {
                    finalValue += String.valueOf((int) letter - 48);
                }
            }
        } catch (Exception e) {
            CreateLogFile("Utils.GetNumericDigits: " + e.getMessage());
        }
        return finalValue;
    }

    private static String GetNumericDigitsOfDriverLicense(String driverLicense) {
        int finalValue = 0;
        try {
            for (int i = 0; i < driverLicense.length(); i++) {
                char letter = driverLicense.charAt(i);
                if (Character.isDigit(letter)) {
                    finalValue += (int) letter - 48;
                }
            }
        } catch (Exception e) {
            CreateLogFile("Utils.GetNumericDigitsOfDriverLicense: " + e.getMessage());
        }

        if (finalValue > 99) {
            return String.valueOf(finalValue).substring(String.valueOf(finalValue).length() - 2, String.valueOf(finalValue).length());
        } else if (finalValue < 10) {
            return "0" + String.valueOf(finalValue);
        }
        return String.valueOf(finalValue);
    }

    public static String DecToHex(int dec) {
        String hex;
        if (dec == 0) return "00";
        hex = String.format("%X", dec);
        if (hex.length() % 2 != 0) hex = "0" + hex;
        return hex;
    }

    private static String RightPad(String str, int num, char pad) {
        return String.format("%1$-" + num + "s", str).replace(' ', pad);
    }

    private static String LeftPad(String str, int length) {
        if (str.length() < length) {
            int amount = length - str.length();
            for (int i = 0; i < amount; i++) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static double ConvertKmToMiles(double value) {
        double result = 0;
        try {
            result = value * 0.621371;
            if (result != 0) result = Math.round(result * 100.0) / 100.0;
        } catch (Exception e) {
        }
        return result;
    }

    public static double ConvertKmhToMPH(double value) {
        double result = 0;
        try {
            result = value * 0.621371;
            if (result != 0) result = Math.round(result * 100.0) / 100.0;
        } catch (Exception e) {
        }
        return result;
    }


    public static String flipHEX(final String hex) {
        final StringBuilder builder = new StringBuilder(hex.length());
        for (int i = hex.length(); i > 1; i -= 2) {
            builder.append(hex.substring(i - 2, i));
        }
        return builder.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String getFMCSAUSBDir() {
        File file = new File("/storage");
        File[] files = file.listFiles();
        for (File temp : files) {
            File fmcsa = new File(temp.getPath() + "/ELDUSBTX.txt");
            if (fmcsa.exists()) {
                //this device
                StringBuilder dataFile = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(fmcsa));
                    String line;
                    while ((line = br.readLine()) != null) {
                        dataFile.append(line);
                    }
                    br.close();
                    String fmcsa_file_data = "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";
                    if (dataFile.toString().equals(fmcsa_file_data)) {
                        return temp.getPath();
                    }
                } catch (Exception e) {
                }
            }
        }
        return "";
    }

    public static boolean copyFileToUSBMemory(File inputFile, String outputPath) {
        boolean result = false;
        try {
            //check if input file exist
            if (inputFile != null && inputFile.length() > 0) {
                //create output directory if it doesn't exist
                File dir = new File(outputPath);
                if (!dir.exists()) dir.mkdirs();

                CreateLogFile("outputFile " + outputPath + inputFile.getName());
                File outputFile = new File(outputPath + inputFile.getName());
                InputStream in = new FileInputStream(inputFile);
                OutputStream out = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                // write the output file (You have now copied the file)
                out.flush();
                out.close();

                outputFile = new File(outputPath + inputFile.getName());
                CreateLogFile("inputFile space " + String.valueOf(inputFile.length()));
                CreateLogFile("outputFile space " + String.valueOf(outputFile.length()));

                if (inputFile.length() == outputFile.length()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            CreateLogFile(e.getMessage());
        }
        return result;
    }

    public static boolean spaceAvailableOnUSBMemory(File inputFile, String outputPath) {
        boolean space = true;
        try {
            File outputFile = new File(outputPath);
            if (outputFile.getFreeSpace() > inputFile.length()) {
                space = true;
            }
        } catch (Exception e) {
        }
        return space;
    }

    public static boolean validateVin(String vin) {
        boolean result = false;
        try {
            if (vin.length() == 17 && !vin.contains("I") && !vin.contains("O") && !vin.contains("Q")) {
                Hashtable<String, Integer> map = new Hashtable<>();
                int[] equivalents = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
                for (int i = 1; i < 10; i++) {
                    map.put(String.valueOf(i), i);
                    switch (i) {
                        case 1:
                            map.put("A", i);
                            map.put("J", i);
                            break;
                        case 2:
                            map.put("B", i);
                            map.put("K", i);
                            map.put("S", i);
                            break;
                        case 3:
                            map.put("C", i);
                            map.put("L", i);
                            map.put("T", i);
                            break;
                        case 4:
                            map.put("D", i);
                            map.put("M", i);
                            map.put("U", i);
                            break;
                        case 5:
                            map.put("E", i);
                            map.put("N", i);
                            map.put("V", i);
                            break;
                        case 6:
                            map.put("F", i);
                            map.put("W", i);
                            break;
                        case 7:
                            map.put("G", i);
                            map.put("P", i);
                            map.put("X", i);
                            break;
                        case 8:
                            map.put("H", i);
                            map.put("Y", i);
                            break;
                        case 9:
                            map.put("R", i);
                            map.put("Z", i);
                            break;
                    }
                }

                int sum = 0;
                int length = vin.length();
                int i = 0;
                String letter;
                while (i < length) {
                    letter = vin.substring(i, i + 1);
                    if (!letter.equals("0")) {
                        sum += map.get(letter) * equivalents[i];
                    }
                    i++;
                }

                String valueToCompare = "X";
                int mod = sum % 11;
                if (mod >= 0 && mod <= 9) {
                    valueToCompare = String.valueOf(mod);
                }
                letter = vin.substring(8, 9);
                if (valueToCompare.equals(letter)) result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }


    public static double ConvertMilesToKm(double value) {
        double result = 0;
        try {
            result = value * 1.60934;
            if (result != 0) {
                result = Math.round(result * 100.0) / 100.0;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static double ConvertLitersToGallons(double value) {
        double result = 0;
        try {
            result = value * 0.264172;
            if (result != 0) {
                result = Math.round(result * 100.0) / 100.0;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static boolean isAppInForeground(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
        } else {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            return appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE;
        }
    }


    public static void DeleteSignatures(List<File> signatureList) {
        for (File file : signatureList) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static void DeleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) file.delete();
    }

    private boolean canToggleGPS() {
        PackageManager pacman = MyApplication.GetAppContext().getPackageManager();
        PackageInfo pacInfo;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                //test if receiver is exported. if so, we can toggle GPS.
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                    return true;
                }
            }
        }

        return false; //default
    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(MyApplication.GetAppContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            MyApplication.GetAppContext().sendBroadcast(poke);
        }
    }

    public static File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DVIRFolder");
        if (!file.exists())
            file.mkdir();

        return file;
    }

    public static String formatReport(Date date, TimeZone timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    public static String formatDate(Date date, TimeZone timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    public static String getSPNFromHexDTC(String dtcCodeHex) {
        if (dtcCodeHex != null && dtcCodeHex.length() >= 6) {
            int mask = Integer.valueOf("E0", 16);
            String temp = dtcCodeHex.substring(4, 6);
            int codeDec = Integer.valueOf(temp, 16);
            int spnDec = codeDec & mask;
            spnDec = spnDec >> 5;
            String spnStr = Utils.DecToHex(spnDec) + dtcCodeHex.substring(2, 4) + dtcCodeHex.substring(0, 2);
            spnDec = Integer.valueOf(spnStr, 16);
            return String.valueOf(spnDec);
        }
        return "";
    }

    public static int getFMIFromHexDTC(String dtcCodeHex) {
        if (dtcCodeHex != null && dtcCodeHex.length() >= 6) {
            int mask = Integer.valueOf("1F", 16);
            String temp = dtcCodeHex.substring(4, 6);
            int codeDec = Integer.valueOf(temp, 16);
            return codeDec & mask;
        }
        return -1;
    }

    public static int getCountFromHexDTC(String dtcCodeHex) {
        if (dtcCodeHex != null && dtcCodeHex.length() >= 8) {
            int mask = Integer.valueOf("7F", 16);
            String temp = dtcCodeHex.substring(6, 8);
            int codeDec = Integer.valueOf(temp, 16);
            return codeDec & mask;
        }
        return 0;
    }

    public static double updateOffsetWithECM(double visualOdometer, double ecmOdometer) {
        return Math.round(visualOdometer - ecmOdometer);
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) {
                return true;

            }

        }
        return false;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static byte[] GeByteFromBitmap(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static String getLocalizedResources(Context context, Locale desiredLocale, int idView) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources().getString(idView);
    }


    public static Resources GetResourceByLocale(final String locale, Context context, WindowManager manager, AssetManager assetManager) {
        Configuration conf = context.getResources().getConfiguration();
        conf.locale = new Locale(locale);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        Resources resources = new Resources(assetManager, metrics, conf);
        return resources;
    }

    public static PedidoDetail findProductDetail(String barCode) {
        for (PedidoDetail pedidoDetail : MySingleton.getInstance().getPedidoResponse().getPedidoDetailList()) {
            if ((pedidoDetail.getCodigoBarra() != null && pedidoDetail.getCodigoBarra().equals(barCode)) || (pedidoDetail.getCb2() != null && pedidoDetail.getCb2().equals(barCode)) || (pedidoDetail.getcBar3() != null && pedidoDetail.getcBar3().equals(barCode))) {
                return pedidoDetail;
            }
        }
        return null;
    }

}

