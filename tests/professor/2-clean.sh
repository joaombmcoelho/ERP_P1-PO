#!/bin/zsh

error_exit() {
    echo "Error: $1" >&2
    echo "Usage: $0 DELIVERY" >&2
    echo "          DELIVERY is one of 'e0', 'ei', or 'ef'" >&2
    exit 1
}

[[ $# -ne 1 ]] && error_exit "argument expected"

DIRECTORY=$1

# misc 
/bin/rm -rf $DIRECTORY/**/.project
/bin/rm -rf $DIRECTORY/**/.cproject
/bin/rm -rf $DIRECTORY/**/.cvsignore
/bin/rm -rf $DIRECTORY/**/.gitignore
/bin/rm -rf $DIRECTORY/**/.settings
/bin/rm -rf $DIRECTORY/**/.docsdone
/bin/rm -rf $DIRECTORY/**/.placeholder
/bin/rm -rf $DIRECTORY/**/.classpath
/bin/rm -rf $DIRECTORY/**/*.class
/bin/rm -rf $DIRECTORY/**/*.jar
/bin/rm -rf $DIRECTORY/**/*.xml
/bin/rm -rf $DIRECTORY/**/*.import
/bin/rm -rf $DIRECTORY/**/*.in
/bin/rm -rf $DIRECTORY/**/*.out
/bin/rm -rf $DIRECTORY/**/[Mm]akefile
/bin/rm -rf $DIRECTORY/**/po-uilib*
/bin/rm -rf $DIRECTORY/**/*~

# other
/bin/rm -rf $DIRECTORY/**/examples
/bin/rm -rf $DIRECTORY/**/tests
/bin/rm -rf $DIRECTORY/**/testes

# po-uilib
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/Dialog.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/Display.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldBoolean.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldInteger.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/Field.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldNone.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldOption.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldReal.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FieldString.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/FormException.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/Form.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/forms/Message.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/InteractionDriver.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/menus/CommandException.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/menus/Command.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/menus/DoOpenMenu.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/menus/Menu.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/menus/Message.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/NewSwingInteraction.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/Prompt.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingDialogForm.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingDialogMenu.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingForm.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingInteraction.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingMenu.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingMessage.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/swing/SwingPanel.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/CompositePrintStream.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/Message.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/Prompt.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/Property.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/RuntimeEOFException.java
/bin/rm -rf $DIRECTORY/**/pt/tecnico/uilib/text/TextInteraction.java

# from project app

/bin/rm -rf $DIRECTORY/**/erp/app/App.java
/bin/rm -rf $DIRECTORY/**/erp/app/basket/BasketCommand.java
/bin/rm -rf $DIRECTORY/**/erp/app/basket/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/basket/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/basket/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/basket/Prompt.java
/bin/rm -rf $DIRECTORY/**/erp/app/client/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/client/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/client/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/client/Prompt.java
/bin/rm -rf $DIRECTORY/**/erp/app/company/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/company/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/company/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/company/Prompt.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/ClientRegistrationFailedException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/CompanyRegistrationFailedException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/FileOpenFailedException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/NotEnoughInventoryException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/ProductNotSuppliedException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/ProductRegistrationFailedException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/UnknownClientKeyException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/UnknownCompanyKeyException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/UnknownProductKeyException.java
/bin/rm -rf $DIRECTORY/**/erp/app/exceptions/UnknownPurchaseKeyException.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/DoOpenMenuClients.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/DoOpenMenuCompanies.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/DoOpenMenuProducts.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/DoOpenMenuPurchases.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/main/Prompt.java
/bin/rm -rf $DIRECTORY/**/erp/app/product/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/product/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/product/Message.java
/bin/rm -rf $DIRECTORY/**/erp/app/product/Prompt.java
/bin/rm -rf $DIRECTORY/**/erp/app/purchase/Label.java
/bin/rm -rf $DIRECTORY/**/erp/app/purchase/Menu.java
/bin/rm -rf $DIRECTORY/**/erp/app/purchase/Prompt.java

# from project core

/bin/rm -rf $DIRECTORY/**/erp/exceptions/UnavailableFileException.java
/bin/rm -rf $DIRECTORY/**/erp/exceptions/ImportFileException.java
/bin/rm -rf $DIRECTORY/**/erp/exceptions/MissingFileAssociationException.java
/bin/rm -rf $DIRECTORY/**/erp/exceptions/UnrecognizedEntryException.java

# clean CVS Id tags

sed -i 's=\$Id: .* \$==g' $DIRECTORY/**/*.java

