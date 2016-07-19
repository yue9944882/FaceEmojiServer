package com.kimmin.faceemoji.server.test.azure.storage;

import com.kimmin.faceemoji.server.Constant.AccountInfo;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.core.Utility;
import org.junit.Test;

/**
 * Created by kimmin on 7/19/16.
 */
public class SimpleAzureStorageTest {

    @Test
    public void saveAnBlob(){
        try{
            System.out.println(AccountInfo.storageConnectionString);
            CloudStorageAccount account = CloudStorageAccount.parse(AccountInfo.storageConnectionString);
            CloudBlobClient client = account.createCloudBlobClient();
            CloudBlobContainer container = client.getContainerReference("imgcontainer");
            container.createIfNotExists();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

}
