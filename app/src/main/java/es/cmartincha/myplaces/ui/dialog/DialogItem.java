
package es.cmartincha.myplaces.ui.dialog;

/**
 * Clase que contiene la informacion de una opcion del dialogo
 */
public class DialogItem {
    /**
     * Icono
     */
    int mIconResource;

    /**
     * Texto
     */
    int mTextResource;

    public DialogItem(int textResource, int iconResource) {
        mTextResource = textResource;
        mIconResource = iconResource;
    }
}
