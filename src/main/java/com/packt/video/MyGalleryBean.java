/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.packt.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

/** This class is responsible to sustain upload process and provide collection 
 * of photos that will be listed in the data table and the gallery.
 *
 * @author kit.perez
 */
@Named(value = "myGalleryBean")
@ViewScoped
public class MyGalleryBean implements Serializable {
    
    private List<MyPhoto> photos = new ArrayList<>();
    private static final int BUFFER_SIZE = 6124;

    /**
     * Creates a new instance of MyGalleryBean
     */
    public MyGalleryBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Path path = Paths.get(((ServletContext) externalContext.getContext())
                .getRealPath(File.separator + "resources" + File.separator + "photos"));
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file: ds) {
                MyPhoto photo = new MyPhoto(file.getFileName().toString(), false);
                photos.add(photo);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyGalleryBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MyPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MyPhoto> photos) {
        this.photos = photos;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        
        FacesContext faceContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = faceContext.getExternalContext();
        
        if (event.getFile() != null) {
            Path path = Paths.get(((ServletContext) externalContext.getContext())
                    .getRealPath(File.separator + "resources" + File.separator + "photos" + File.separator));
            FileOutputStream fileOutputStream;
            InputStream inputStream;
            try {
                String fn = event.getFile().getFileName();
                fileOutputStream = new FileOutputStream(path.toString() + File.separator + fn);
                
                byte[] buffer = new byte[BUFFER_SIZE];
                
                int bulk;
                inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                
                fileOutputStream.close();
                inputStream.close();
                
                MyPhoto newPhoto = new MyPhoto(fn, false);
                photos.add(newPhoto);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MyGalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MyGalleryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            
        }
    }
    
    
}
