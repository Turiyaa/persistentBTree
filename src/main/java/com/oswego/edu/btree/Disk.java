package com.oswego.edu.btree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import com.oswego.edu.btree.BTree.Node;

public class Disk {

	public static long BLOCK_SIZE = 4096;
	ByteBuffer buffer;
	private RandomAccessFile btFile;
	private FileChannel btFileChannel;
	public HashMap<Long, Node> cache = new HashMap<Long, Node>();
	HashMap<Long, ByteBuffer> cacheList;

	public Disk(String fileName) {
		try {
			cacheList = new HashMap<>();
			buffer = ByteBuffer.allocate(4096);
			btFile = new RandomAccessFile("lakerdb/" + fileName + ".txt", "rw");
			btFileChannel = btFile.getChannel();

			// For testing, don't have to delete file for every test
			// btFileChannel.truncate(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void diskWriteMetaData(int degree, int size) throws IOException {
		btFile.seek(0);
		buffer.clear();
		buffer.putInt(degree);
		buffer.putInt(size);
		buffer.putLong(16);
		// cacheList.put(0L, buffer);
		buffer.flip();
		btFileChannel.write(buffer);
		btFileChannel.force(true);
	}

	public int diskReadMetaData(int degree, Node n) throws IOException {
		int size = 0;
		try {
			buffer.clear();
			btFile.seek(0);
			btFileChannel.read(buffer);
			buffer.flip();
			degree = buffer.getInt();
			size = buffer.getInt();
			n.offset = buffer.getLong();

		} catch (IOException e) {
		}
		return size;
	}

	public long nextNodeOffset() throws IOException {

		long nxtOffset = 0;

		try {
			nxtOffset = btFile.length();
		} catch (Exception e) {
		}
		;
		return nxtOffset;
	}

	public void diskWrite(Node x, int t) throws IOException {
		cache.put(x.offset, x);
		btFile.seek(x.offset);
		buffer.clear();
		byte isLeaf;
		if (x.isLeaf)
			isLeaf = 0;
		else
			isLeaf = 1;
		try {
			buffer.putLong(x.offset);
			// 1. put number of keys
			buffer.putInt(x.numKey);

			// 2. save is leaf or not
			buffer.put(isLeaf);
			// write the treeObjs
			// check to see if filler needed for all keys
			for (int i = 0; i < 2 * t - 1; i++) {
				// for(int i = 0; i < x.numKey; i++){
				// Edited to make long value
				byte[] incodeKey = x.keys[i].key.getBytes();

				// 3. put key length which is fixed to 40
				buffer.putInt(incodeKey.length);

				// 4. put key, stuffed with empty spaces
				buffer.put(incodeKey, 0, incodeKey.length);

				// 5. put val
				buffer.putInt(x.keys[i].val);
			}
			// put the childNodes in the buffer
			for (long children : x.childern) {
				buffer.putLong(children);
			}
			buffer.flip();
/*			ByteBuffer cacheBuffer;
			if (!cacheList.containsKey(x.offset)) {
				cacheBuffer = buffer.slice();
				cacheList.put(x.offset, cacheBuffer);
			}*/
			btFileChannel.write(buffer);
			btFileChannel.force(true);
			buffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Node diskRead(long offset, int t) {
		if(cache.containsKey(offset)) {
			return cache.get(offset);
		}
		Node x = new Node();
		byte isLeaf;
		buffer.clear();
		try {
/*			if (cacheList.containsKey(offset)) {
				buffer = cacheList.get(offset);
				buffer.rewind();
			} else {
				btFile.seek(offset);
				btFileChannel.read(buffer);
				buffer.flip();

			}*/

			btFile.seek(offset);
			btFileChannel.read(buffer);
			buffer.flip();

			// 1.read numKey
			x.offset = buffer.getLong();
			x.numKey = buffer.getInt();
			// get the leaf
			// 2. read leaf
			isLeaf = buffer.get();
			if (isLeaf == 0)
				x.isLeaf = true;
			else
				x.isLeaf = false;
			// put the treeObjs into node x

			for (int i = 0; i < 2 * t - 1; i++) {
				// for(int i = 0; i < x.numKey; i++) {
				// 3. read length to get key
				int length = buffer.getInt();
				byte[] decodeKey = new byte[length];
				// 4. get key
				buffer.get(decodeKey, 0, length);
				// set key
				x.keys[i].key = new String(decodeKey);
				// 5. set key
				x.keys[i].val = buffer.getInt();
			}
			// put the childNodes in the buffer
			// size of childern isn't matching, debug
			for (int i = 0; i < 2 * t - 1; i++)
				x.childern[i] = buffer.getLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cache.put(x.offset, x);
		return x;

	}

	public boolean isEmpty() {
		try {
			return (btFile.length() == 0);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void writeRootNode(Node n) {
		try {
			btFile.seek(8);
			buffer.clear();
			buffer.putLong(n.offset);
			buffer.flip();
			btFileChannel.write(buffer);
			btFileChannel.force(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeTreeSize(int size) {
		try {
			btFile.seek(4);
			buffer.clear();
			buffer.putInt(size);
			buffer.flip();
			btFileChannel.write(buffer);
			btFileChannel.force(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
