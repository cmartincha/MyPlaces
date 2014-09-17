package es.cmartincha.myplaces.ui.principal.ui.map;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LocationErrorDialogFragment extends DialogFragment {
    private Dialog mDialog;

    public LocationErrorDialogFragment() {
        super();
        mDialog = null;
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}