package com.kimmin.faceemoji.server.test.azure.storage;

import com.kimmin.faceemoji.server.constant.AccountInfo;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by kimmin on 7/19/16.
 */
public class SimpleAzureStorageTest {


    private CloudBlobContainer container = null;

    @Before
    public void connectToAzureStorage(){
        try{
            CloudStorageAccount account = CloudStorageAccount.parse(AccountInfo.storageConnectionString);
            CloudBlobClient client = account.createCloudBlobClient();
            CloudBlobContainer container = client.getContainerReference("imgcontainer");
            this.container = container;
            container.createIfNotExists();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


    @Test
    public void saveBlob(){
        try{
            CloudBlockBlob blob = this.container.getBlockBlobReference("test_blob");
            String str = "Hello, Azure";
            blob.upload(new ByteArrayInputStream(str.getBytes()), str.getBytes().length);
            System.out.println(blob.getStorageUri());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @Test
    public void getBlob(){
        try{
            CloudBlockBlob blob = this.container.getBlockBlobReference("test_blob");
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            blob.download(ostream);
            System.out.println(new String(ostream.toByteArray()));
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteBlob(){
        try{
            CloudBlockBlob blob = this.container.getBlockBlobReference("test_blob");
            blob.deleteIfExists();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

}
