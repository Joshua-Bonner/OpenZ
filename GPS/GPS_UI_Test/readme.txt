GPS and UI integration test.
Verify that GPS and UI function ok together.

GPS_UI_Test missing three folders due to size restrictions of regular Github. The folders can be found at the following links:
GreaterLAMobileMap: https://drive.google.com/open?id=1RvGCegsyk5p_5QOOo83TpCe7P8VpzZMA
Linux runtime: https://drive.google.com/open?id=1Gil5o58Oo4vRBrcO8ghgnTkkNosxFFoE
JavaFX SDk: https://drive.google.com/open?id=1Xkbdmt5PT8nq3it4a7caOyyoAm42D2MC

Folder Structure:
UI
|-- .idea
|-- jniLibs
|     |-- LX64
|     |-- directX
|-- lib
|-- libs
|-- openJFX
|     |-- openjfx-11.0.2_linux
|     |       |-- javafx-sdk-11.0.2
|     |       |       |-- legal
|     |       |       |-- lib
|-- out
|-- resources
|-- src
| ...
|-- Greater_Los_Angeles.mmpk
| ...

if the project is looking for the javafx stuff, in Intellij, go to 
1. Run -> Edit Configurations
2. Create a new Application type config called: TurnByTurn
3. In the Main class field, enter: TurnByTurn
4. In the VM options, enter : 
              --module-path
              ./openjfx/openjfx-11.0.2_linux/javafx-sdk-11.0.2/lib
              --add-modules=javafx.controls,javafx.fxml,javafx.web,javafx.swing,javafx.media
5. Click OK.
6. Build -> Build Project
7. Run

The address to enter is: 19102 Kemp Ave Carson, Ca
