package demo.thaonguyen.showimage;

import java.util.Date;

public class Block {
    int blockID;
    String data;
    String hash;
    String preHash;
    Date timeStamp;
    int nonce;

    public Block(int blockID, String data, String hash, String preHash, Date timeStamp, int nonce) {
        this.blockID = blockID;
        this.data = data;
        this.hash = hash;
        this.preHash = preHash;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
    }

    public Block() {
    }

    public int getBlockID() {
        return blockID;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreHash() {
        return preHash;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public int getNonce() {
        return nonce;
    }
}
