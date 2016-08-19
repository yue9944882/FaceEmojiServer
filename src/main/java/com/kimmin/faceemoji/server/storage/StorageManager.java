package com.kimmin.faceemoji.server.storage;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kimmin on 7/19/16.
 */
public class StorageManager {

    private StorageManager(){
        try {
            this.account = CloudStorageAccount.parse(AccountInfo.storageConnectionString);
            this.client = this.account.createCloudBlobClient();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    private static class Singleton {
        private static StorageManager instance = new StorageManager();
    }
    public static StorageManager getInstance(){
        return Singleton.instance;
    }

    private CloudStorageAccount account = null;
    private CloudBlobClient client = null;

    public String saveImageToCloud(String username, InputStream stream, long length, String givenUuid){
        try {
            CloudBlobContainer container = client.getContainerReference(username);
            boolean newed = false;
            while(!container.exists()) {
                newed = container.createIfNotExists();
            }
            if(newed){
                BlobContainerPermissions p = container.downloadPermissions();
                while(! p.getPublicAccess().equals(BlobContainerPublicAccessType.CONTAINER)) {
                    BlobContainerPermissions permissions = new BlobContainerPermissions();
                    permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                    container.uploadPermissions(permissions);
                    p = container.downloadPermissions();
                }
            }
            String uuid = givenUuid == null ? UUID.randomUUID().toString() : givenUuid;
            CloudBlockBlob blob = container.getBlockBlobReference(uuid);
            blob.getProperties().setContentType("image/png");
            blob.upload(stream, length);
            return blob.getStorageUri().getPrimaryUri().toString();
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }


    public List<String> getPhotoByOwner(String username){
        try{
            List<String> uris = new ArrayList<String>();
            CloudBlobContainer pcontainer = client.getContainerReference("p" + username);
            Iterator<ListBlobItem> iter = pcontainer.listBlobs().iterator();
            while(iter.hasNext()){
                ListBlobItem blobItem = iter.next();
                uris.add(blobItem.getStorageUri().getPrimaryUri().toString());
            }
            return uris;
        }catch (Throwable e){
            e.printStackTrace();
            return new LinkedList();
        }
    }


    public void deletePhoto(String username, String key){
        try{
            CloudBlobContainer pcontainer = client.getContainerReference("p" + username);
            CloudBlobContainer container = client.getContainerReference(username);
            CloudBlockBlob pblob = pcontainer.getBlockBlobReference(key);
            CloudBlockBlob blob = container.getBlockBlobReference(key);
            pblob.deleteIfExists();
            blob.deleteIfExists();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}
