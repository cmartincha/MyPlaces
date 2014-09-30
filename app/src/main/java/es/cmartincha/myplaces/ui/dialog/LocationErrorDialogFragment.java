package es.cmartincha.myplaces.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Dialogo de error en el servicio de localizacion de google
 */
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