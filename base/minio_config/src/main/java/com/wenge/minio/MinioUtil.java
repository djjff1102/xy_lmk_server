package com.wenge.minio;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.SpringUtil;
import io.minio.*;
import io.minio.messages.DeleteObject;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * minio常用操作
 */
public class MinioUtil {
	private final static MinioClient minioClient;

	private final static String bucket;

	private final static String split = "@quesoar@";

	static {
		minioClient = SpringUtil.getBean("minioClient", MinioClient.class);
		if (minioClient == null) {
			throw new NullPointerException("minioClient is null");
		}
		MinioConfig minioConfig = SpringUtil.getBean("minioConfig", MinioConfig.class);
		if (minioConfig == null) {
			throw new NullPointerException("minioConfig is null");
		}
		bucket = minioConfig.getBucket();
	}


	public static String upload(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		return upload(file.getOriginalFilename(), file.getInputStream(), file.getSize(), true);
	}

	public static String upload(String fileName, InputStream inputStream, long fileSize, boolean newName) throws Exception {
		try (InputStream fileInputStream = inputStream) {
			if (newName) {
				fileName = IdWorker.getIdStr() + split + fileName;
			}
			minioClient.putObject(PutObjectArgs.builder()
					.bucket(bucket)
					.object(fileName)
					.stream(fileInputStream, fileSize, -1)
					.build());
			return fileName;
		}
	}


	//文件下载
	public static void download(HttpServletResponse response, String fileName) throws Exception {
		try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket)
				.object(fileName).build())) {
			//获取文件源信息
			StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
					.bucket(bucket)
					.object(fileName)
					.build());
			//设置响应的内容类型 --浏览器对应不同类型做不同处理
			response.setContentType(statObject.contentType());
			//设置响应头
			response.setHeader("Content-Disposition", "attachment;filename=" +
					URLEncoder.encode(getRealFileName(fileName), "UTF-8"));
			//利用apache的IOUtils
			IOUtils.copy(inputStream, response.getOutputStream());
		}
	}

	//文件下载
	public static void download(OutputStream outputStream, String fileName) throws Exception {
		try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket)
				.object(fileName).build())) {
			//获取文件源信息
			StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
					.bucket(bucket)
					.object(fileName)
					.build());
			//利用apache的IOUtils
			IOUtils.copy(inputStream, outputStream);
		}
	}

	public static String getRealFileName(String fileName) {
		if (CommonUtil.isNotBlank(fileName) && fileName.contains(split)) {
			return fileName.split(split)[1];
		}
		return fileName;
	}

	public static void delete(String fileName) throws Exception {
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
	}

	public static void delete(Collection<String> fileNames) {
		minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucket)
				.objects(fileNames.stream().map(DeleteObject::new).collect(Collectors.toList())).build());
	}

	//
	////获取文件流
	//public InputStream getInput(String bucketName, String filename) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
	//	return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filename).build());
	//}
	//
	////获取minio指定文件对象信息
	//public StatObjectResponse getStatObject(String bucketName, String filename) {
	//	StatObjectResponse statObject = null;
	//
	//	try {
	//		statObject = minioClient.statObject(StatObjectArgs.builder()
	//				.bucket(bucketName)
	//				.object(filename)
	//				.build());
	//	} catch (XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException |
	//	         IOException |
	//	         InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return statObject;
	//}
	//
	////删除桶 --(不是空桶也删)
	//public boolean removeBucket(String bucketName) {
	//	try {
	//		List<Object> folderList = this.getFolderList(bucketName);
	//		List<String> fileNames = new ArrayList<>();
	//		if (folderList != null && !folderList.isEmpty()) {
	//			for (int i = 0; i < folderList.size(); i++) {
	//				Map o = (Map) folderList.get(i);
	//				String name = (String) o.get("fileName");
	//				fileNames.add(name);
	//			}
	//		}
	//		if (!fileNames.isEmpty()) {
	//			for (String fileName : fileNames) {
	//				this.delete(bucketName, fileName);
	//			}
	//		}
	//		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
	//		return Boolean.TRUE;
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return Boolean.FALSE;
	//}
	//
	//////文件上传
	////public String upload(String bucketName, MultipartFile file) {
	////	//返回客户端文件系统中的原始文件名
	////	String originalFilename = file.getOriginalFilename();
	////	String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
	////	String fileName = System.currentTimeMillis() + suffix;
	////	InputStream inputStream = null;
	////	try {
	////		inputStream = file.getInputStream();
	////		minioClient.putObject(PutObjectArgs.builder()
	////				.bucket(bucketName)
	////				.object(fileName)
	////				.stream(inputStream, file.getSize(), -1)
	////				.build());
	////		return fileName;
	////	} catch (Exception e) {
	////		Log.logger.error(e.getMessage(),e);
	////	} finally {
	////		if (inputStream != null) {
	////			try {
	////				inputStream.close();
	////			} catch (IOException e) {
	////				Log.logger.error(e.getMessage(),e);
	////			}
	////		}
	////	}
	////	return null;
	////}
	//
	////文件上传
	//public String upload(String bucketName, InputStream inputStream, String fileName) {
	//	//返回客户端文件系统中的原始文件名
	//	try {
	//		minioClient.putObject(PutObjectArgs.builder()
	//				.bucket(bucketName)
	//				.object(fileName)
	//				.stream(inputStream, inputStream.available(), -1)
	//				.build());
	//		return fileName;
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	} finally {
	//		if (inputStream != null) {
	//			try {
	//				inputStream.close();
	//			} catch (IOException e) {
	//				Log.logger.error(e.getMessage(),e);
	//			}
	//		}
	//	}
	//	return null;
	//}
	//
	//
	////文件删除
	//public boolean delete(String bucketName, String fileName) {
	//	try {
	//		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
	//				.object(fileName).build());
	//		return Boolean.TRUE;
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return Boolean.FALSE;
	//}
	//
	////桶是否存在
	//public boolean bucketExists(String bucketName) {
	//	boolean b = false;
	//	try {
	//		b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	//		if (b) {
	//			return Boolean.TRUE;
	//		}
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return Boolean.FALSE;
	//}
	//
	////创建桶
	//public boolean createBucket(String bucketName) {
	//	try {
	//		boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	//		if (!b) {
	//			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
	//		}
	//		return Boolean.TRUE;
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return Boolean.FALSE;
	//}
	//
	////获取桶列表
	//public List getBucketList() throws Exception {
	//	List<Bucket> buckets = minioClient.listBuckets();
	//	List list = new ArrayList();
	//	for (Bucket bucket : buckets) {
	//		String name = bucket.name();
	//		list.add(name);
	//	}
	//	return list;
	//}
	//
	////获取指定bucketName下所有文件 文件名+大小
	//public List<Object> getFolderList(String bucketName) throws Exception {
	//	Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
	//	Iterator<Result<Item>> iterator = results.iterator();
	//	List<Object> items = new ArrayList<>();
	//	String format = "{'fileName':'%s','fileSize':'%s'}";
	//	while (iterator.hasNext()) {
	//		Item item = iterator.next().get();
	//		items.add(JSON.parse((String.format(format, item.objectName(),
	//				formatFileSize(item.size())))));
	//	}
	//	return items;
	//}
	//
	///**
	// * 讲快文件合并到新桶   块文件必须满足 名字是 0 1  2 3 5....
	// *
	// * @param bucketName  存块文件的桶
	// * @param bucketName1 存新文件的桶
	// * @param fileName1   存到新桶中的文件名称
	// * @return
	// */
	//public boolean merge(String bucketName, String bucketName1, String fileName1) {
	//	try {
	//		List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
	//		List<Object> folderList = this.getFolderList(bucketName);
	//		List<String> fileNames = new ArrayList<>();
	//		if (folderList != null && !folderList.isEmpty()) {
	//			for (int i = 0; i < folderList.size(); i++) {
	//				Map o = (Map) folderList.get(i);
	//				String name = (String) o.get("fileName");
	//				fileNames.add(name);
	//			}
	//		}
	//		if (!fileNames.isEmpty()) {
	//			Collections.sort(fileNames, new Comparator<String>() {
	//				@Override
	//				public int compare(String o1, String o2) {
	//					if (Integer.parseInt(o2) > Integer.parseInt(o1)) {
	//						return -1;
	//					}
	//					return 1;
	//				}
	//			});
	//			for (String name : fileNames) {
	//				sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(name).build());
	//			}
	//		}
	//
	//		minioClient.composeObject(
	//				ComposeObjectArgs.builder()
	//						.bucket(bucketName1)
	//						.object(fileName1)
	//						.sources(sourceObjectList)
	//						.build());
	//		return Boolean.TRUE;
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//	return Boolean.FALSE;
	//}
	//
	////预览
	//public void preview(HttpServletResponse response, String bucketName, String filename) {
	//	try {
	//		InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filename).build());
	//		response.setContentType("application/octet-stream");
	//		response.setCharacterEncoding("UTF-8");
	//		ServletOutputStream outputStream = response.getOutputStream();
	//		IOUtils.copy(inputStream, outputStream);
	//	} catch (Exception e) {
	//		Log.logger.error(e.getMessage(),e);
	//	}
	//}
	//
	//private static String formatFileSize(long fileS) {
	//	DecimalFormat df = new DecimalFormat("#.00");
	//	String fileSizeString = "";
	//	String wrongSize = "0B";
	//	if (fileS == 0) {
	//		return wrongSize;
	//	}
	//	if (fileS < 1024) {
	//		fileSizeString = df.format((double) fileS) + " B";
	//	} else if (fileS < 1048576) {
	//		fileSizeString = df.format((double) fileS / 1024) + " KB";
	//	} else if (fileS < 1073741824) {
	//		fileSizeString = df.format((double) fileS / 1048576) + " MB";
	//	} else {
	//		fileSizeString = df.format((double) fileS / 1073741824) + " GB";
	//	}
	//	return fileSizeString;
	//}
	//

}
