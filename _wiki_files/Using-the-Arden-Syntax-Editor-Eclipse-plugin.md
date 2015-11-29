## Overview

Using the Arden Syntax Editor Eclipse plugin is pretty much straightforward.  
There is the Editor which highlights Medical Logic Module (MLM) syntax and then there is the launcher which starts Arden2ByteCode with the MLM currently being edited.

## Using the Editor

To start using the Editor, you have to open a MLM file in a project. This may be any project type.  
If you doubleclick an existing MLM or create a new one, Eclipse should ask you whether you want to add the xText nature to that project. Click "yes".  
Now the Editor should be active for all *.mlm files.

## Using the launcher

If you have opened a MLM file, you can start Arden2ByteCode by right-clicking on that file and selecting 
**Run As... -> Medical Logic Module**.  
This is the quick way to launch a MLM.

Further, you can edit the command line options being passed to Arden2ByteCode by creating and editing a launch configuration in the **Run Configurations.../Debug Configurations...** dialog. This dialog is accessible via the **Run** entry in the main menu or via the option in the Run/Debug button popup.

When starting a MLM, the output should be displayed in the console window. You may also be prompted for input in that window if you programmed your MLM that way.

## Support

If you have further questions regarding the plugin, use the E-Mail contact on the following page:  
<http://arden2bytecode.sf.net/contact>