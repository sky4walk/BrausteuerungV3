// brausteuerung@AndreBetz.de

package de.mikrosikaru.brausteuerungapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.FileReader;
import java.util.ArrayList;

public class BrewRecipeFileBeerXml implements BrewRecipeFile {
    private static final String TAG = "BrewRecipeFileBeerXml";
    private String mError = "";

    private String mBeerXmlSubDirTags[] = {
            "RECIPES", "RECIPE", "HOPS", "HOP", "FERMENTABLES", "FERMENTABLE",
            "YEASTS", "YEAST", "MISCS", "MASH", "MASH_STEPS", "MASH_STEP"
    };

    private String mBeerXmlNameTags[] = {
            "VERSION","NAME","TYPE","BREWER","BATCH_SIZE","BOIL_TIME",
            "EFFICIENCY","ALPHA","AMOUNT","USE","TIME","FORM","YIELD","COLOR",
            "GRAIN_TEMP","STEP_TEMP","STEP_TIME"
    };

    private boolean mBeerXmlSubDirActive[];
    private boolean mBeerXmlNameActive[];
    private ArrayList<SystemBrewStep> meshList = new ArrayList();
    private ArrayList<SystemBrewStep> hopList = new ArrayList();

    private int isTag(String tagName) {
        for ( int i = 0; i < mBeerXmlSubDirTags.length; i++) {
            if ( 0 == tagName.compareToIgnoreCase(mBeerXmlSubDirTags[i]))
                return i;
        }
        return -1;
    }

    private int isName(String tagName) {
        for ( int i = 0; i < mBeerXmlNameTags.length; i++) {
            if ( 0 == tagName.compareToIgnoreCase(mBeerXmlNameTags[i]))
                return i;
        }
        return -1;
    }

    public BrewRecipeFileBeerXml() {
        mBeerXmlSubDirActive = new boolean[mBeerXmlSubDirTags.length];
        for ( int i = 0; i < mBeerXmlSubDirTags.length; i++ ) {
            mBeerXmlSubDirActive[i] = false;
        }
        mBeerXmlNameActive = new boolean[mBeerXmlNameTags.length];
        for ( int i = 0; i < mBeerXmlNameTags.length; i++ ) {
            mBeerXmlNameActive[i] = false;
        }
    }
    @Override
    public boolean Load(SystemSettings settings, String fileName) {
        mError = "";

        meshList.clear();
        hopList.clear();
        SystemBrewStep actMeshStep = null;
        SystemBrewStep actHopStep = null;
        int boilTime = 0;

        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(new FileReader(fileName));

            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                event = myParser.next();

                String name = myParser.getName();
                String text = myParser.getText();

                switch (event) {
                    case XmlPullParser.START_TAG: {
                        int pos = isTag(name);
                        if (0 <= pos) {
                            mBeerXmlSubDirActive[pos] = true;
                        } else {
                            pos = isName(name);
                            if ( 0 <= pos) {
                                mBeerXmlNameActive[pos] = true;
                            }
                        }

                        if ( isTag(name) == isTag("MASH_STEP")) {
                            actMeshStep = new SystemBrewStep();
                            actMeshStep.setNr(meshList.size());
                            actMeshStep.setOn(true);
                        } else if ( isTag(name) == isTag("HOP")) {
                            actHopStep = new SystemBrewStep();
                            actHopStep.setOn(true);
                            actHopStep.setHalt(true);
                            actHopStep.setCall(true);
                            actHopStep.setSollTemp(100);
                            actHopStep.setMaxTemp( 5.0f);
                            actHopStep.setMinTemp(-5.0f);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        int pos = isTag(name);
                        if (0 <= pos) {
                            mBeerXmlSubDirActive[pos] = false;
                        } else {
                            pos = isName(name);
                            if ( 0 <= pos) {
                                mBeerXmlNameActive[pos] = false;
                            }
                        }

                        if ( isTag(name) == isTag("MASH_STEP")) {
                            meshList.add(actMeshStep);
                        } else if ( isTag(name) == isTag("HOP")) {
                            insertInHopfenList(hopList,actHopStep);
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {

                        if (   mBeerXmlSubDirActive[isTag("RECIPES")] ) {
                            if ( mBeerXmlSubDirActive[isTag("RECIPE")] ) {
                                if ( mBeerXmlSubDirActive[isTag("HOPS")] ) {
                                    if ( mBeerXmlSubDirActive[isTag("HOP")] ) {
                                        if ( mBeerXmlNameActive[isName("VERSION")] ) {
                                        } else if ( mBeerXmlNameActive[isName("NAME")] ) {
                                            String str = actHopStep.getInfoField();
                                            str += "Hopfen: " + text +" ";
                                            actHopStep.setInfoField(str);
                                            actHopStep.setName(text);
                                        } else if ( mBeerXmlNameActive[isName("ALPHA")] ) {
                                            String str = actHopStep.getInfoField();
                                            str += "Alpha: " + text +" ";
                                            actHopStep.setInfoField(str);
                                        } else if ( mBeerXmlNameActive[isName("AMOUNT")] ) {
                                            String str = actHopStep.getInfoField();
                                            str += "Menge: " + text +" ";
                                            actHopStep.setInfoField(str);
                                        } else if ( mBeerXmlNameActive[isName("USE")] ) {
                                        } else if ( mBeerXmlNameActive[isName("TIME")] ) {
                                            actHopStep.setTime(Integer.parseInt(text));
                                        } else if ( mBeerXmlNameActive[isName("FORM")] ) {
                                        }
                                    }
                                } else if ( mBeerXmlSubDirActive[isTag("FERMENTABLES")] ) {
                                    if ( mBeerXmlSubDirActive[isTag("FERMENTABLE")] ) {
                                        if ( mBeerXmlNameActive[isName("VERSION")] ) {
                                        } else if ( mBeerXmlNameActive[isName("NAME")] ) {
                                            String str = settings.getBrewRecipeDescription();
                                            str += "Malz Name : " + text + " ";
                                            settings.setBrewRecipeDescription(str);
                                        } else if ( mBeerXmlNameActive[isName("TYPE")] ) {
                                        } else if ( mBeerXmlNameActive[isName("AMOUNT")] ) {
                                            String str = settings.getBrewRecipeDescription();
                                            str += "Malz Menge : " + text + " ";
                                            settings.setBrewRecipeDescription(str);
                                        } else if ( mBeerXmlNameActive[isName("YIELD")] ) {
                                        } else if ( mBeerXmlNameActive[isName("COLOR")] ) {
                                        }
                                    }
                                } else if ( mBeerXmlSubDirActive[isTag("YEASTS")] ) {
                                    if ( mBeerXmlSubDirActive[isTag("YEAST")] ) {
                                        if ( mBeerXmlNameActive[isName("VERSION")] ) {
                                        } else if ( mBeerXmlNameActive[isName("NAME")] ) {
                                            String str = settings.getBrewRecipeDescription();
                                            str += "Hefe Name: " + text + " ";
                                            settings.setBrewRecipeDescription(str);
                                        } else if ( mBeerXmlNameActive[isName("TYPE")] ) {
                                        } else if ( mBeerXmlNameActive[isName("FORM")] ) {
                                        } else if ( mBeerXmlNameActive[isName("AMOUNT")] ) {
                                            String str = settings.getBrewRecipeDescription();
                                            str += "Hefe Menge: " + text + " ";
                                            settings.setBrewRecipeDescription(str);
                                        }
                                    }
                                } else if ( mBeerXmlSubDirActive[isTag("MASH")] ) {
                                    if ( mBeerXmlSubDirActive[isTag("MASH_STEPS")] ) {
                                        if ( mBeerXmlSubDirActive[isTag("MASH_STEP")] ) {
                                            if (mBeerXmlNameActive[isName("VERSION")]) {
                                            } else if (mBeerXmlNameActive[isName("NAME")]) {
                                                actMeshStep.setName(text);
                                            } else if (mBeerXmlNameActive[isName("TYPE")]) {
                                            } else if (mBeerXmlNameActive[isName("STEP_TEMP")]) {
                                                actMeshStep.setSollTemp(Float.parseFloat(text));
                                                actMeshStep.setMinTemp(-0.1f);
                                                actMeshStep.setMaxTemp(-0.1f);
                                                actMeshStep.setMaxGradient(1.0f);
                                            } else if (mBeerXmlNameActive[isName("STEP_TIME")]) {
                                                actMeshStep.setTime(Integer.parseInt(text));
                                            }
                                        }
                                    } else {
                                        if ( mBeerXmlNameActive[isName("VERSION")] ) {
                                        } else if ( mBeerXmlNameActive[isName("NAME")] ) {
                                        } else if ( mBeerXmlNameActive[isName("TYPE")] ) {
                                        } else if ( mBeerXmlNameActive[isName("GRAIN_TEMP")] ) {
                                        }
                                    }
                                } else {
                                    if ( mBeerXmlNameActive[isName("VERSION")] ) {
                                    } else if (mBeerXmlNameActive[isName("NAME")] ) {
                                        String str = settings.getBrewRecipeDescription();
                                        str += "Name : " + text + " ";
                                        settings.setBrewRecipeDescription(str);
                                    } else if ( mBeerXmlNameActive[isName("TYPE")] ) {
                                    } else if ( mBeerXmlNameActive[isName("BREWER")] ) {
                                    } else if ( mBeerXmlNameActive[isName("BATCH_SIZE")] ) {
                                        String str = settings.getBrewRecipeDescription();
                                        str += "Sudmenge : " + text + " ";
                                        settings.setBrewRecipeDescription(str);
                                    } else if ( mBeerXmlNameActive[isName("BOIL_TIME")] ) {
                                        boilTime = Integer.parseInt(text);
                                    } else if ( mBeerXmlNameActive[isName("EFFICIENCY")] ) {
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }

            int maxRastNr = addMeshStepsBrewSteps(meshList,settings);
            int restTime = corectTime(hopList,boilTime);

            actHopStep = new SystemBrewStep();
            actHopStep.setOn(true);
            actHopStep.setCall(true);
            actHopStep.setHalt(true);
            actHopStep.setName("Nachkochen");
            actHopStep.setTime(restTime);
            actHopStep.setSollTemp(100);
            actHopStep.setMaxGradient(1.0f);
            actHopStep.setMinTemp(-5.0f);
            actHopStep.setMaxTemp( 5.0f);
            hopList.add(actHopStep);

            maxRastNr = addHopfStepsBrewSteps(hopList,settings,maxRastNr);

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
            SystemBrewStep actStep = hopfenSteps.get(i);
            if ( actStep.getTime() < step.getTime() ) {
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

    int addMeshStepsBrewSteps(
            ArrayList<SystemBrewStep> meshSteps,
            SystemSettings settings) {
        int cnt = 0;
        for ( int i = 0; i < meshSteps.size() && i < settings.getBrewSteps(); i++ ) {
            SystemBrewStep actSetp = meshSteps.get(i);
            settings.getBrewStep(i).copy(actSetp);
            cnt++;
        }
        return cnt;
    }

    int addHopfStepsBrewSteps(
            ArrayList<SystemBrewStep> hopfenSteps,
            SystemSettings settings,
            int startRast) {
        for ( int i = 0; i < hopfenSteps.size() && startRast < settings.getBrewSteps(); i++ ) {
            SystemBrewStep actSetp = hopfenSteps.get(i);
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

    @Override
    public String getError() {
        return mError;
    }
}
