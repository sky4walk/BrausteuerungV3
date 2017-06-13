// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by andre on 09.12.2016.
 */

public class BrewRecipeFileBml implements BrewRecipeFile {

    private static final String TAG = "BrewRecipeFileBml";

    private String mError = "";
    public String getError() {
        return mError;
    }
    public boolean Load(SystemSettings settings, String fileName) {
        FileInputStream fis = null;
        mError = "";

        try {
            fis = new FileInputStream(fileName);
        }catch(IOException e){
            mError = TAG + "FileInputStream IOException exception: - " + e.toString();
            return false;
        }

        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(fis, null);

            int rastNr = 0;
            int depth = 0;
            boolean inRast = false;
            boolean inRecipe = false;
            boolean tagBegin = false;
            boolean inDesc = false;
            String actTag = "";
            SystemBrewStep brewStep = new SystemBrewStep();

            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT)
            {
                String name = myParser.getName();
                String text = myParser.getText();
                switch (event) {
                    case XmlPullParser.START_TAG: {
                        tagBegin = true;
                        if ( 0 == name.compareToIgnoreCase("recipe")) {
                            inRecipe = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("rast")) {
                            inRast = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("description")) {
                            inDesc = true;
                            depth++;
                        } else {
                            if ( inRast ) {
                                actTag = name;
                            }
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        tagBegin = false;
                        if ( 0 == name.compareToIgnoreCase("recipe")) {
                            inRecipe = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("rast")) {
                            inRast = false;
                            depth--;
                            actTag = "";
                            if ( rastNr < settings.getBrewSteps() )
                                settings.getBrewStep(rastNr).copy(brewStep);
                            brewStep.reset();
                        } else if ( 0 == name.compareToIgnoreCase("description")) {
                            inDesc = false;
                            depth--;
                        }else {
                            actTag = "";
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        try {
                            if ( 0 < actTag.length() ) {
                                if (inRecipe) {
                                    if (0 == actTag.compareToIgnoreCase("Nr")) {
                                        rastNr = Integer.parseInt(text);
                                        brewStep.setNr(rastNr);
                                    } else if (0 == actTag.compareToIgnoreCase("Name")) {
                                        brewStep.setName(text);
                                    } else if (0 == actTag.compareToIgnoreCase("On")) {
                                        brewStep.setOn(Boolean.parseBoolean(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Halt")) {
                                        brewStep.setHalt(Boolean.parseBoolean(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Call")) {
                                        brewStep.setCall(Boolean.parseBoolean(text));
                                    } else if (0 == actTag.compareToIgnoreCase("SollTemp")) {
                                        brewStep.setSollTemp(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Time")) {
                                        brewStep.setTime(Integer.parseInt(text));
                                    } else if (0 == actTag.compareToIgnoreCase("MinTemp")) {
                                        brewStep.setMinTemp(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("MaxTemp")) {
                                        brewStep.setMaxTemp(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Kp")) {
                                        brewStep.setKp(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Ki")) {
                                        brewStep.setKi(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Kd")) {
                                        brewStep.setKd(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("OnTimePulse")) {
                                        brewStep.setOnTimePuls(Integer.parseInt(text));
                                    } else if (0 == actTag.compareToIgnoreCase("OffTimePulse")) {
                                        brewStep.setOffTimePuls(Integer.parseInt(text));
                                    } else if (0 == actTag.compareToIgnoreCase("MaxGradient")) {
                                        brewStep.setMaxGradient(Float.parseFloat(text));
                                    } else if (0 == actTag.compareToIgnoreCase("Info")) {
                                        brewStep.setInfoField(text);
                                    }
                                }
                            } else if ( 0 < text.length() ) {
                                if (inDesc) {
                                    settings.setBrewRecipeDescription(text);
                                }
                            }
                        } catch( Exception e ) {
                            mError = TAG + " LoadBrewRecipe IOException exception:" + name + " " + e.toString();
                            return false;
                        }
                        break;
                    }
                }
                event = myParser.next();
            }

        } catch (Exception e) {
            mError = TAG + " XmlPullParser exception: - " + e.toString();
            return false;
        }
        return true;
    }
    public boolean Save(SystemSettings settings, String fileName) {
        mError = "";
        FileOutputStream fos;
        File tempFile = new File(fileName);

        try {
            fos = new FileOutputStream(tempFile, false);
        } catch(IOException e){
            mError = TAG + " FileOutputStream IOException exception: - " + e.toString();
            return false;
        }

        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "recipe");
            serializer.attribute(null, "AppDevice", settings.getAppDevice());
            serializer.attribute(null, "AppName", settings.getAppName());
            serializer.attribute(null, "AppVersion", settings.getAppVersion());

            serializer.startTag(null, "description");
            serializer.text(settings.getBrewRecipeDescription());
            serializer.endTag(null, "description");

            for (int i = 0; i < settings.getBrewSteps(); i++) {
                SystemBrewStep brewStep = settings.getBrewStep(i);
                serializer.startTag(null, "rast");

                serializer.startTag(null, "Nr");
                serializer.text(Integer.toString(brewStep.getNr()));
                serializer.endTag(null, "Nr");

                serializer.startTag(null, "Name");
                serializer.text(brewStep.getName());
                serializer.endTag(null, "Name");

                serializer.startTag(null, "On");
                serializer.text(Boolean.toString(brewStep.isOn()));
                serializer.endTag(null, "On");

                serializer.startTag(null, "Halt");
                serializer.text(Boolean.toString(brewStep.isHalt()));
                serializer.endTag(null, "Halt");

                serializer.startTag(null, "Call");
                serializer.text(Boolean.toString(brewStep.isCall()));
                serializer.endTag(null, "Call");

                serializer.startTag(null, "SollTemp");
                serializer.text(Float.toString(brewStep.getSollTemp()));
                serializer.endTag(null, "SollTemp");

                serializer.startTag(null, "Time");
                serializer.text(Integer.toString(brewStep.getTime()));
                serializer.endTag(null, "Time");

                serializer.startTag(null, "MinTemp");
                serializer.text(Float.toString(brewStep.getMinTemp()));
                serializer.endTag(null, "MinTemp");

                serializer.startTag(null, "MaxTemp");
                serializer.text(Float.toString(brewStep.getMaxTemp()));
                serializer.endTag(null, "MaxTemp");

                serializer.startTag(null, "Kp");
                serializer.text(Float.toString(brewStep.getKp()));
                serializer.endTag(null, "Kp");

                serializer.startTag(null, "Ki");
                serializer.text(Float.toString(brewStep.getKi()));
                serializer.endTag(null, "Ki");

                serializer.startTag(null, "Kd");
                serializer.text(Float.toString(brewStep.getKd()));
                serializer.endTag(null, "Kd");

                serializer.startTag(null, "OnTimePulse");
                serializer.text(Integer.toString(brewStep.getOnTimePuls()));
                serializer.endTag(null, "OnTimePulse");

                serializer.startTag(null, "OffTimePulse");
                serializer.text(Integer.toString(brewStep.getOffTimePuls()));
                serializer.endTag(null, "OffTimePulse");

                serializer.startTag(null, "MaxGradient");
                serializer.text(Float.toString(brewStep.getMaxGradient()));
                serializer.endTag(null, "MaxGradient");

                serializer.startTag(null, "Info");
                serializer.text(brewStep.getInfoField());
                serializer.endTag(null, "Info");

                serializer.endTag(null, "rast");
            }

            serializer.endTag(null, "recipe");

            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch(IOException e) {
            mError = TAG + " XmlSerializer IOException exception: - " + e.toString();
            return false;
        }
        return true;
    }

}
