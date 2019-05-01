package app.facedetection;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Files {

    private static final String TAG = "Files";

    private static String mSelfieDir;

    /**
     * Selfie File Management
     */
    private static void initSelfie(Context c) {
        mSelfieDir = c.getFilesDir().getAbsolutePath() + "/Compressed_Selfies/";
        String mUploadedSelfieDir = c.getFilesDir().getAbsolutePath() + "/Uploaded_Selfies/";
        String[] s = {mSelfieDir, mUploadedSelfieDir};
        for (String s1 : s) {
            File file = new File(s1);
            if (!file.exists()) file.mkdir();
        }
    }

    public static String getSelfieDir(Context c) {
        initSelfie(c);
        return mSelfieDir;
    }

    public static File getImagePath(Context context, String name) {
        initSelfie(context);
        return new File(mSelfieDir, name);
    }

    public static String saveSelfie(Context c, byte[] b, String s) {
        initSelfie(c);
        File f = new File(mSelfieDir, s);
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(b);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String p = f.getAbsolutePath();
//        Logger.i(TAG, "saveSelfie: " + p);
//        Log.e(TAG, "saveSelfie:asdf " +Compressor.init(p) );
//        return Compressor.init(p);

        return p;
    }

//    public static void moveSelfie(Context context, String mFileName) {
//        long iSelfieLength = 0, iUploadedSelfieLength = 0;
//        File mSelfie, mUploadedSelfie;
//
//        mSelfie = new File(mSelfieDir, mFileName);
//
//        if (mSelfie.exists()) {
//            iSelfieLength = mSelfie.length();
//
//            mUploadedSelfie = new File(mUploadedSelfieDir, mFileName);
//
//            try {
//                FileInputStream in = new FileInputStream(mSelfie);
//                FileOutputStream out = new FileOutputStream(mUploadedSelfie);
//
//                byte[] buffer = new byte[1024];
//                int read;
//                while ((read = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, read);
//                }
//                in.close();
//
//                out.flush();
//                out.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            iUploadedSelfieLength = mUploadedSelfie.length();
//        }
//
//        if (iSelfieLength == iUploadedSelfieLength)
//            Logger.i(TAG, "moveSelfie: move successful");
//        else
//            Logger.i(TAG, "moveSelfie: move un-successful");
//    }

    public static void deleteSelfies(Context c, String cid) {
        initSelfie(c);
        for (File f : new File(mSelfieDir).listFiles()) {
            String fp = f.getAbsolutePath();
            if (fp.contains(cid))
                if (f.delete()) Log.i(TAG, "deleteSelfies: ClassId: " + cid + " File: " + fp);
        }
    }

//    public static boolean isImageExists(String path) {
//        File file = new File(path);
//        return file.exists() && file.length() > 0;
//    }


//    private String readFromFile(Context context) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput("config.txt");
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString;
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        } catch (FileNotFoundException e) {
//            Logger.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Logger.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return ret;
//    }
//
//    public static void write(String imageString) {
//        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Timble/", "imageData.txt");
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            out.write(imageString.getBytes());
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
