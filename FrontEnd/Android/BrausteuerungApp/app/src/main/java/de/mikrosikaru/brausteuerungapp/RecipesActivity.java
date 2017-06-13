// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {
    private static Button mBtn_back;
    private static Button mBtn_new;
    private static Button mBtn_load;
    private static Button mBtn_save;
    private static Button mBtn_send;
    private static EditText mET_Description;
    private static ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private static ArrayList<Button> mButtonList = new ArrayList<Button>();
    private static int mListViewIDs = Constants.RAST_ID_START;
    private RecipeStepFragment mOpenRecipeFragment = null;
    private GlobalVars getGlobalVars() {
        return (GlobalVars) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_recipes);

        mBtn_back = (Button) findViewById(R.id.btn_id_recipde_back);
        mBtn_back.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             finish();
                                         }
                                     }
        );

        mBtn_new = (Button) findViewById(R.id.btn_id_recipde_new);
        mBtn_new.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            clearSettings();
                                        }
                                    }
        );

        mBtn_load = (Button) findViewById(R.id.btn_id_recipde_load);
        mBtn_load.setOnClickListener(new View.OnClickListener() {
                                        String m_chosen;
                                         @Override
                                         public void onClick(View v) {
                                             SimpleFileDialog FileLoadDialog =  new SimpleFileDialog(RecipesActivity.this, "FileLoad",
                                                     new SimpleFileDialog.SimpleFileDialogListener()
                                                     {
                                                         @Override
                                                         public void onChosenDir(String chosenDir)
                                                         {
                                                             m_chosen = chosenDir;
                                                             String fileExt = getString(R.string.file_str_file_ending);
                                                             String fileExtXSud = getString(R.string.file_str_file_ending_import1);
                                                             String fileExtBX = getString(R.string.file_str_file_ending_import2);
                                                             String getExt = SimpleFileDialog.getExtension(m_chosen);
                                                             clearSettings();
                                                             if ( 0 == getExt.
                                                                        compareToIgnoreCase(fileExt) ){
                                                                 BrewRecipeFile recipeFile = new BrewRecipeFileBml();
                                                                 if ( recipeFile.Load(getGlobalVars().getSettings(),m_chosen) ) {
                                                                     updateSettings();
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load) + recipeFile.getError() +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 } else {
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load_err) +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 }
                                                             } else if ( 0 == getExt.
                                                                     compareToIgnoreCase(fileExtXSud) ){
                                                                 BrewRecipeFileXsud recipeFileXsud = new BrewRecipeFileXsud();
                                                                 if ( recipeFileXsud.Load(getGlobalVars().getSettings(),m_chosen) ) {
                                                                     updateSettings();
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load) +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 } else {
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load_err) + recipeFileXsud.getError() +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 }
                                                             } else if ( 0 == getExt.
                                                                     compareToIgnoreCase(fileExtBX) ){
                                                                 BrewRecipeFileBeerXml recipeFileBS = new BrewRecipeFileBeerXml();
                                                                 if ( recipeFileBS.Load(getGlobalVars().getSettings(),m_chosen) ) {
                                                                     updateSettings();
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load) +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 } else {
                                                                     Toast.makeText(RecipesActivity.this,
                                                                             getString(R.string.file_str_file_load_err) + recipeFileBS.getError() +
                                                                                     m_chosen, Toast.LENGTH_LONG).show();
                                                                 }
                                                             }

                                                         }
                                                     });

                                             FileLoadDialog.Default_File_Name = "";
                                             FileLoadDialog.chooseFile_or_Dir();
                                         }
                                     }
        );

        mBtn_save = (Button) findViewById(R.id.btn_id_recipde_save);
        mBtn_save.setOnClickListener(new View.OnClickListener() {
                                         String m_chosen;

                                         @Override
                                         public void onClick(View v) {
                                             SimpleFileDialog FileSaveDialog =
                                                     new SimpleFileDialog(RecipesActivity.this,
                                                             "FileSave",
                                                             new SimpleFileDialog.SimpleFileDialogListener() {
                                                                 @Override
                                                                 public void onChosenDir(String chosenDir) {
                                                                     m_chosen = chosenDir;

                                                                     String fileExt = getString(R.string.file_str_file_ending);
                                                                     if (0 != SimpleFileDialog.
                                                                             getExtension(m_chosen).
                                                                             compareToIgnoreCase(fileExt)) {
                                                                         m_chosen += "." + fileExt;
                                                                     }

                                                                     BrewRecipeFile recipeFile = new BrewRecipeFileBml();
                                                                     if (recipeFile.Save(getGlobalVars().getSettings(),m_chosen)) {
                                                                         Toast.makeText(RecipesActivity.this,
                                                                                 getString(R.string.file_str_file_save) +
                                                                                         m_chosen, Toast.LENGTH_LONG).show();
                                                                     } else {
                                                                         Toast.makeText(RecipesActivity.this,
                                                                                 getString(R.string.file_str_file_save_err) + recipeFile.getError() +
                                                                                         m_chosen, Toast.LENGTH_LONG).show();
                                                                     }

                                                                 }
                                                             });

                                             FileSaveDialog.Default_File_Name = "Recipe.bml";
                                             FileSaveDialog.chooseFile_or_Dir();
                                         }
                                     }
        );

        mBtn_send = (Button) findViewById(R.id.btn_id_recipde_ok);
        mBtn_send.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             
                                             String tmpFileName = SimpleFileDialog.getFilePath(getApplicationContext());
                                             tmpFileName += "/" + getApplicationContext().getString(R.string.file_str_file_defaut_name);
                                             tmpFileName += getGlobalVars().getConnectedMacAdressConverted();
                                             tmpFileName += "." + getString(R.string.file_str_file_ending);
											 SimpleFileDialog.deleteFile(tmpFileName);
											 
											 getGlobalVars().startBrewStepsStore();
                                             finish();
                                         }
                                     }
        );


        if (savedInstanceState != null) {
            //closeFragment();
        }

        addButtons();

        if ( !getGlobalVars().isChatConnected() ) {
            mBtn_send.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateSettings();
        //closeFragments();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ( null != mOpenRecipeFragment ) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(mOpenRecipeFragment);
            mOpenRecipeFragment = null;
        }
       // closeFragment();
    }

    private void enableButtons(boolean enable) {
        if ( getGlobalVars().isChatConnected() ) {
            mBtn_send.setEnabled(enable);
        } else {
            mBtn_send.setEnabled(false);
        }
        mBtn_back.setEnabled(enable);
        mBtn_new.setEnabled(enable);
        mBtn_load.setEnabled(enable);
        mBtn_save.setEnabled(enable);
        for ( int i = 0; i < mButtonList.size(); i++) {
            Button btn = (Button) mButtonList.get(i);
            btn.setEnabled(enable);
        }
    }
    private void updateSettings() {
        for (int i = 0; i < getGlobalVars().getSettings().getBrewSteps(); i++) {
            Button btn = (Button) mButtonList.get(i);
            RecipeStepFragment recFrag = (RecipeStepFragment) mFragmentList.get(i);
            if ( recFrag.isAdded() ) {
                recFrag.readSettingValues();
            }
            btn.setText(getGlobalVars().getSettings().getBrewStep(i).getName());

            if ( getGlobalVars().getSettings().getBrewStep(i).isOn() )
                btn.getBackground().setColorFilter(getResources().
                        getColor(R.color.colorButtonActivate), PorterDuff.Mode.MULTIPLY);
            else
                btn.getBackground().setColorFilter(getResources().
                        getColor(R.color.colorButtonDeactivate), PorterDuff.Mode.MULTIPLY);
        }
        mET_Description.setText(getGlobalVars().getSettings().getBrewRecipeDescription());
    }
    private void clearSettings() {
        for (int i = 0; i < getGlobalVars().getSettings().getBrewSteps(); i++) {
            getGlobalVars().getSettings().getBrewStep(i).copy(new SystemBrewStep());
            getGlobalVars().getSettings().getBrewStep(i).setNr(i);
            getGlobalVars().getSettings().getBrewStep(i).setName(
                    getString(R.string.title_recipe_name) + Integer.toString(i + 1));
            Button btn = (Button) mButtonList.get(i);
            RecipeStepFragment recFrag = (RecipeStepFragment) mFragmentList.get(i);
            if ( recFrag.isAdded() ) {
                recFrag.readSettingValues();
            }
            btn.setText(getGlobalVars().getSettings().getBrewStep(i).getName());

            if ( getGlobalVars().getSettings().getBrewStep(i).isOn() )
                btn.getBackground().setColorFilter(getResources().
                        getColor(R.color.colorButtonActivate), PorterDuff.Mode.MULTIPLY);
            else
                btn.getBackground().setColorFilter(getResources().
                        getColor(R.color.colorButtonDeactivate), PorterDuff.Mode.MULTIPLY);
        }
        mET_Description.setText("");
        getGlobalVars().getSettings().setBrewRecipeDescription("");
    }
    private void closeFragment() {
        FragmentManager fm = getFragmentManager();


        for (int i = 0; i < mFragmentList.size(); i++) {
            RecipeStepFragment recipeFragment = (RecipeStepFragment) mFragmentList.get(i);
                if (!recipeFragment.isHidden()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(recipeFragment);
                    ft.commit();
                }
        }
    }
    private void addButtons() {
        mFragmentList.clear();
        mButtonList.clear();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recipes_id_scrollLinear);

        for (int i = 0; i < getGlobalVars().getSettings().getBrewSteps(); i++) {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            RecipeStepFragment recipeFrag = new RecipeStepFragment();
            recipeFrag.setRastNumber(i);
            mFragmentList.add(recipeFrag);

            Button btn = new Button(this);
            btn.setText(getGlobalVars().getSettings().getBrewStep(i).getName());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    int pos = mButtonList.indexOf(btn);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    RecipeStepFragment recipeFragment = (RecipeStepFragment) mFragmentList.get(pos);

                    if (recipeFragment.isAdded()) {
                        if (recipeFragment.isHidden()) {
                            enableButtons(false);
                            btn.setEnabled(true);
                            ft.show(recipeFragment);
                            mOpenRecipeFragment = recipeFragment;
                        } else {
                            mOpenRecipeFragment = null;
                            enableButtons(true);
                            ft.hide(recipeFragment);
                            btn.setText(getGlobalVars().getSettings().getBrewStep(pos).getName());
                        }
                    } else {
                        enableButtons(false);
                        btn.setEnabled(true);
                        ft.add(mListViewIDs + pos, recipeFragment);
                        mOpenRecipeFragment = recipeFragment;
                    }

                    if ( getGlobalVars().getSettings().getBrewStep(pos).isOn() )
                        btn.getBackground().setColorFilter(getResources().
                                getColor(R.color.colorButtonActivate), PorterDuff.Mode.MULTIPLY);
                    else
                        btn.getBackground().setColorFilter(getResources().
                                getColor(R.color.colorButtonDeactivate), PorterDuff.Mode.MULTIPLY);

                    ft.commit();
                }
            } );
            mButtonList.add(btn);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(btn, layoutParams);

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setId(mListViewIDs + i);
            linearLayout.addView(ll);
        }

        mET_Description = new EditText(getApplicationContext());
        RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        mET_Description.setBackgroundResource(R.drawable.bg_grey);
        mET_Description.setBackgroundColor(Color.WHITE);
        mET_Description.setTextColor(Color.BLACK);
        mET_Description.setText("");
        mET_Description.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    getGlobalVars().getSettings().setBrewRecipeDescription(
                            mET_Description.getText().toString());
                }
            }
        });
        mET_Description.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                getGlobalVars().getSettings().setBrewRecipeDescription(
                        mET_Description.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        linearLayout.addView(mET_Description);
    }
}
