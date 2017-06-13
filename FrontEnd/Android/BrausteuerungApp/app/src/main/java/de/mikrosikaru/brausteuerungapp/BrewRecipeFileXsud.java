// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andre on 09.12.2016.
 */

public class BrewRecipeFileXsud implements BrewRecipeFile {
    private static final String TAG = "BrewRecipeFileXsud";
    private String mError = "";

    public String getError() {
        return mError;
    }

    private int getNrAfter(String complete, String pre) {
        int nr = 0;
        String after = complete.substring(complete.lastIndexOf(pre) + pre.length()) ;
        try {
            nr = Integer.parseInt(after);
        } catch ( Exception e ) {

        }
        return nr;
    }

    @Override
    public boolean Load(SystemSettings settings, String fileName) {
        mError = "";

        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(new FileReader(fileName));

            int rastNr = 0;
            int hopfenNr = 0;
            int maxHopfNr = 0;
            int maxRastNr = 0;
            int kochDauer = 0;
            int isoDauer = 0;
            int depth = 0;
            int countRast = 0;
            int countHopfen = 0;
            float einMaischTemp = 0;
            boolean inSud = false;
            boolean inRecipe = false;
            boolean inRasten = false;
            boolean inRastNr = false;
            boolean tagBegin = false;
            boolean inHopfen = false;
            boolean inAnteil = false;
            boolean inVersion = false;
            boolean inKochDauer = false;
            boolean inEinmaischeTemp = false;
            boolean inIsoDauer = false;
            String actTag = "";
            SystemBrewStep brewStep = new SystemBrewStep();

            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                event = myParser.next();

                String name = myParser.getName();
                String text = myParser.getText();

                switch (event) {
                    case XmlPullParser.START_TAG: {
                        tagBegin = true;
                        if ( 0 == name.compareToIgnoreCase("xsud") && 0 == depth) {
                            inRecipe = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Sud")) {
                            inSud = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Rasten")) {
                            inRasten = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Hopfengaben")) {
                            inHopfen = true;
                            depth++;
                        } else if ( -1 < name.indexOf("Anteil_")) {
                            inAnteil = true;
                            if ( inHopfen )
                                countHopfen++;
                            depth++;
                        } else if ( -1 < name.indexOf("Rast_")) {
                            inRastNr = true;
                            if (inRasten)
                                countRast++;
                            depth++;
                        } else if (0 == name.compareToIgnoreCase("KochdauerNachBitterhopfung")) {
                            inKochDauer = true;
                            depth++;
                        } else if (0 == name.compareToIgnoreCase("Nachisomerisierungszeit")) {
                            inIsoDauer = true;
                            depth++;
                        } else if (0 == name.compareToIgnoreCase("EinmaischenTemp")) {
                            inEinmaischeTemp = true;
                            depth++;
                        } else {
                            if (inRastNr) {
                                actTag = name;
                            } else if (inRecipe && !inRastNr) {
                                actTag = name;
                            }
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        tagBegin = false;
                        if ( 0 == name.compareToIgnoreCase("xsud") && 1 == depth) {
                            inRecipe = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Version")) {
                            inVersion = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Sud")) {
                            inSud = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Rasten")) {
                            inRasten = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Hopfengaben")) {
                            inHopfen = false;
                            depth--;
                        } else if ( -1 < name.indexOf("Anteil_")) {
                            inAnteil = false;
                            depth--;
                            actTag = "";
                        } else if (0 == name.compareToIgnoreCase("KochdauerNachBitterhopfung")) {
                            inKochDauer = false;
                            depth--;
                        } else if (0 == name.compareToIgnoreCase("Nachisomerisierungszeit")) {
                            inIsoDauer = false;
                            depth--;
                        } else if (0 == name.compareToIgnoreCase("EinmaischenTemp")) {
                            inEinmaischeTemp = false;
                            depth--;
                        } else if ( -1 < name.indexOf("Rast_")) {
                            inRastNr = false;
                            depth--;
                            actTag = "";
                        } else {
                            actTag = "";
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        if ( 0 < text.length() ) {
                            if ( inKochDauer && inSud ) {
                                countRast++;
                                kochDauer = Integer.parseInt(text);
                            } else if ( inIsoDauer && inSud ) {
                                countRast++;
                                isoDauer = Integer.parseInt(text);
                            } else if (inEinmaischeTemp) {
                                countRast++;
                                einMaischTemp = Float.parseFloat(text);
                            }
                        }
                        break;
                    }
                }
            }

            depth = 0;
            inSud = false;
            inRecipe = false;
            inRasten = false;
            inRastNr = false;
            tagBegin = false;
            inHopfen = false;
            inAnteil = false;
            inVersion = false;

            int hopfenKochen = kochDauer;

            myParser = xmlFactoryObject.newPullParser();
            //fis.getChannel().position(0);
            myParser.setInput(new FileReader(fileName));

            ArrayList<SystemBrewStep> hopfenSteps = new ArrayList<SystemBrewStep>();

            SystemBrewStep actHopfenStep = new SystemBrewStep();

            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT)
            {
                String name = myParser.getName();
                String text = myParser.getText();
                switch (event) {
                    case XmlPullParser.START_TAG: {
                        tagBegin = true;
                        if ( 0 == name.compareToIgnoreCase("xsud") && 0 == depth) {
                            inRecipe = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Version")) {
                            inVersion = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Sud")) {
                            inSud = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Rasten")) {
                            inRasten = true;
                            depth++;
                        } else if ( 0 == name.compareToIgnoreCase("Hopfengaben")) {
                            inHopfen = true;
                            depth++;
                        } else if ( -1 < name.indexOf("Anteil_")) {
                            inAnteil = true;
                            if ( inHopfen ) {
                                hopfenNr = getNrAfter(name,"Anteil_");
                                if (hopfenNr > maxHopfNr) maxHopfNr = hopfenNr;
                                actHopfenStep = new SystemBrewStep();
                                actHopfenStep.reset();
                            }
                            depth++;
                        } else if ( -1 < name.indexOf("Rast_")) {
                            inRastNr = true;
                            rastNr = getNrAfter(name,"Rast_");
                            if ( rastNr > maxRastNr ) maxRastNr = rastNr;
                            depth++;
                        } else {
                            if (inRastNr) {
                                actTag = name;
                            } else if (inRecipe && !inRastNr) {
                                actTag = name;
                            }
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        tagBegin = false;
                        if ( 0 == name.compareToIgnoreCase("xsud") && 1 == depth) {
                            inRecipe = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Sud")) {
                            inSud = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Rasten")) {
                            inRasten = false;
                            depth--;
                        } else if ( 0 == name.compareToIgnoreCase("Hopfengaben")) {
                            inHopfen = false;
                            depth--;
                        } else if ( -1 < name.indexOf("Anteil_")) {
                            if (inHopfen) {
                                insertInHopfenList(hopfenSteps, actHopfenStep);
                            }
                            inAnteil = false;
                            depth--;
                            actTag = "";
                        } else if ( -1 < name.indexOf("Rast_")) {
                            inRastNr = false;
                            depth--;
                            actTag = "";
                            if ( rastNr < settings.getBrewSteps() )
                                settings.getBrewStep(rastNr).copy(brewStep);
                            brewStep.reset();
                        } else {
                            actTag = "";
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        try {
                            if ( 0 < actTag.length() ) {
                                if ( inSud ) {
                                    if (0 == actTag.compareToIgnoreCase("EinmaischenTemp")) {
                                        brewStep.setOn(true);
                                        brewStep.setCall(true);
                                        brewStep.setHalt(true);
                                        brewStep.setNr(0);
                                        brewStep.setName("Einmaischen");
                                        brewStep.setTime(0);
                                        brewStep.setMaxGradient(1.0f);
                                        brewStep.setMinTemp(-0.1f);
                                        brewStep.setMaxTemp(0.1f);
                                        brewStep.setSollTemp(einMaischTemp);
                                        settings.getBrewStep(0).copy(brewStep);
                                        brewStep.reset();
                                    }
                                    if (inRastNr) {
                                        if (0 == actTag.compareToIgnoreCase("RastAktiv")) {
                                            brewStep.setNr(rastNr);
                                            if (0 == text.compareToIgnoreCase("1")) {
                                                brewStep.setOn(true);
                                            }
                                            brewStep.setMaxGradient(1.0f);
                                            brewStep.setMinTemp(-0.1f);
                                            brewStep.setMaxTemp(0.1f);
                                        } else if (0 == actTag.compareToIgnoreCase("RastName")) {
                                            brewStep.setName(text);
                                        } else if (0 == actTag.compareToIgnoreCase("RastTemp")) {
                                            brewStep.setSollTemp(Float.parseFloat(text));
                                        } else if (0 == actTag.compareToIgnoreCase("Halt")) {
                                        } else if (0 == actTag.compareToIgnoreCase("RastDauer")) {
                                            brewStep.setTime(Integer.parseInt(text));
                                        }
                                    } else if (inHopfen) {
                                        if (0 == actTag.compareToIgnoreCase("Aktiv")) {
                                            actHopfenStep.setCall(Boolean.parseBoolean(text));
                                        } else if (0 == actTag.compareToIgnoreCase("Zeit")) {
                                            actHopfenStep.setTime(Integer.parseInt(text));
                                        } else if (0 == actTag.compareToIgnoreCase("Name")) {
                                            actHopfenStep.setName(text);
                                        } else if (0 == actTag.compareToIgnoreCase("erg_Menge")) {
                                            actHopfenStep.setInfoField(text + "g");
                                        }

                                    }
                                } else if ( inVersion ) {
                                    if (0 == actTag.compareToIgnoreCase("xsud")) {
                                        int actVersion = Integer.parseInt(text);
                                        if ( actVersion != Constants.xsudVersion1 ) {
                                            String xSudError = "Wrong XSud Version";
                                            throw new Exception(xSudError + ":" + text);
                                        }
                                    }
                                }
                            }
                        } catch( Exception e ) {
                            mError = TAG + "LoadBrewRecipe IOException exception:" + name + " " + e.toString();
                            return false;
                        }
                        break;
                    }
                }
                event = myParser.next();
            }

            int restTime = corectTime(hopfenSteps,kochDauer);


            maxRastNr = addHopfStepsBrewSteps(hopfenSteps,settings,maxRastNr+1);
            //settings.getBrewStep(maxRastNr-1).setHalt(false);
            //settings.getBrewStep(maxRastNr-1).setCall(false);

            if ( maxRastNr < settings.getBrewSteps() ) {
                settings.getBrewStep(maxRastNr).setOn(true);
                settings.getBrewStep(maxRastNr).setCall(true);
                settings.getBrewStep(maxRastNr).setHalt(true);
                settings.getBrewStep(maxRastNr).setNr(maxRastNr);
                settings.getBrewStep(maxRastNr).setName("Nachkochen");
                settings.getBrewStep(maxRastNr).setTime(restTime);
                settings.getBrewStep(maxRastNr).setSollTemp(100.0f);
                settings.getBrewStep(maxRastNr).setMaxGradient(0.0f);
                settings.getBrewStep(maxRastNr).setMinTemp(-0.1f);
                settings.getBrewStep(maxRastNr).setMaxTemp(0.1f);
                maxRastNr++;
            }

            if ( maxRastNr < settings.getBrewSteps() ) {
                settings.getBrewStep(maxRastNr).setOn(true);
                settings.getBrewStep(maxRastNr).setCall(true);
                settings.getBrewStep(maxRastNr).setHalt(true);
                settings.getBrewStep(maxRastNr).setNr(maxRastNr);
                settings.getBrewStep(maxRastNr).setName("Whirlpool");
                settings.getBrewStep(maxRastNr).setTime(20);
                settings.getBrewStep(maxRastNr).setSollTemp(0.0f);
                settings.getBrewStep(maxRastNr).setMaxGradient(1.0f);
                settings.getBrewStep(maxRastNr).setMinTemp(-0.1f);
                settings.getBrewStep(maxRastNr).setMaxTemp(0.1f);
            }

        } catch (Exception e) {
            mError = TAG + " XmlPullParser exception: - " + e.toString();
            return false;
        }

        return true;
    }

    void insertInHopfenList(ArrayList<SystemBrewStep> hopfenSteps,SystemBrewStep step) {
        boolean found = false;
        for ( int i = 0; i < hopfenSteps.size(); i++ ) {
            SystemBrewStep actSetp = hopfenSteps.get(i);
            if ( actSetp.getTime() < step.getTime() ) {
                hopfenSteps.add(i,step);
                found = true;
                break;
            }
        }

        if ( false == found ) {
            hopfenSteps.add(step);
        }
    }

    int corectTime(ArrayList<SystemBrewStep> hopfenSteps,int actBoilTime) {
        int restTime = actBoilTime;
        for ( int i = 0; i < hopfenSteps.size(); i++ ) {
            SystemBrewStep actSetp = hopfenSteps.get(i);
            int time = actSetp.getTime();
            if ( time < restTime) {
                restTime = time;
            }
            int diffTime = actBoilTime - time;
            actSetp.setTime(diffTime);
        }
        for ( int i = 1; i < hopfenSteps.size(); i++ ) {
            SystemBrewStep actSetp = hopfenSteps.get(hopfenSteps.size()-i);
            SystemBrewStep befSetp = hopfenSteps.get(hopfenSteps.size()-i-1);
            int timeDiff = actSetp.getTime() - befSetp.getTime();
            actSetp.setTime(timeDiff);
        }
        return restTime;
    }

    int addHopfStepsBrewSteps(
            ArrayList<SystemBrewStep> hopfenSteps,
            SystemSettings settings,
            int startRast) {
        for ( int i = 0; i < hopfenSteps.size() && startRast < settings.getBrewSteps(); i++ ) {
            SystemBrewStep actSetp = hopfenSteps.get(i);
            actSetp.setOn(true);
            actSetp.setSollTemp(100.0f);
            actSetp.setCall(true);
            actSetp.setHalt(true);
            actSetp.setNr(startRast);
            settings.getBrewStep(startRast).copy(actSetp);
            startRast++;
        }
        return startRast;
    }
    @Override
    public boolean Save(SystemSettings settings, String fileName) {
        return false;
    }
}
