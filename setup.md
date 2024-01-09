# Basic project setup and dependencies
## JavaFX
* Download & install SceneBuilder from https://gluonhq.com/products/javafx/
* Add SDK to project:
`File > Project Structure > Libraires > + > Java > <Filepath to JavaFX lib folder>`
## Scenebuilder (not mandatory)
* Download & install SceneBuilder from https://gluonhq.com/products/scene-builder/
* After installation, specify SceneBuilder Path in IntelliJ:
  `File > Settings > Languages & Frameworks > JavaFX > Path to SceneBuilder`
## MaterialFX
* add the following lines to **build.gradle**:
```
repositories {
    mavenCentral()
}
dependencies {
    implementation 'io.github.palexdev:materialfx:11.17.0'
}
```
* Open FXML File in SceneBuilder (right click > open in SceneBuilder)
* Add MaterialFX components:
```
  settings icon right to Library view > JAR/FXML Manager > Search repositories > io.github.palexdev:materialfx
``` 
