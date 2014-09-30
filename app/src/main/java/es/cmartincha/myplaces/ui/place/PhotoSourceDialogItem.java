
package es.cmartincha.myplaces.ui.place;

public class PhotoSourceDialogItem {
    static final int DIALOG_CAMERA_OPTION = 0;
    static final int DIALOG_GALLERY_OPTION = 1;
    static final int DIALOG_NO_PHOTO_OPTION = 2;
    int mIconResource;
    int mTextResource;

    public PhotoSourceDialogItem(int textResource, int iconResource) {
        mTextResource = textResource;
        mIconResource = iconResource;
    }
}
