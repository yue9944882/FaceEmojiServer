package com.kimmin.faceemoji.server.storage;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.InputStream;
import java.util.UUID;
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

    public String saveImageToCloud(String username, InputStream stream, long length){
        try {
            CloudBlobContainer container = client.getContainerReference(username);
            container.createIfNotExists();
            UUID uuid = UUID.randomUUID();
            CloudBlockBlob blob = container.getBlockBlobReference(uuid.toString());
            blob.upload(stream, length);
            return uuid.toString();
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }
    }



}
