/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2019.
 */
package dev.galasa.zosfile;

import java.util.Map;

import dev.galasa.zos.IZosImage;
import dev.galasa.zosfile.IZosDataset.DSType;

/**
 * <p>
 * Tester facing interface for zOS file management.
 * </p>
 * 
 * <p>
 * Provides 3 types of files:
 * <ul>
 * <li>IUNIXFile</li>
 * <li>IDataset</li>
 * <li>IVSAMDataset</li>
 * </ul>
 * Typical usage would be to instantiate one of these using the newXXX(...)
 * methods (e.g.: {@link #newDataset(String)}), then to call methods on that
 * object to configure it, and then to call methods on this manager to
 * manipulate it.
 * </p>
 * 
 */
public interface IZosFileHandler {

	/**
	 * Instantiate a new {@link IZosDataset}, which can represent either an
	 * existing dataset, or one to be created.  Member name will be ignored
	 * 
	 * @param dsName
	 * @param image
	 * @return
	 * @throws ZosDatasetException 
	 */
	public IZosDataset newDataset(String dsName, IZosImage image) throws ZosDatasetException;

	/**
	 * Instantiate a new {@link IZosUNIXFile}, which can represent either an
	 * existing UNIX file, or directory, or one to be created. <br />
	 * <br />
	 * N.B. If a directory is to be represented, fullFilePath must end with a
	 * "/"
	 * 
	 * @param fullFilePath
	 * @return
	 * @throws ZosUNIXFileException 
	 */
	public IZosUNIXFile newUNIXFile(String fullFilePath, IZosImage image) throws ZosUNIXFileException;

	/**
	 * APF authorise the given {@link IZosDataset} on the given zOS image.
	 * 
	 * @param dataset
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void apfAuthorise(IZosDataset dataset, IZosImage image) throws ZosDatasetException;

	/**
	 * Return true if the given {@link IZosDataset} is found in the APF-Auth list
	 * for the given zOS image.
	 * 
	 * @param dataset
	 * @param image
	 * @return
	 * @throws ZosDatasetException
	 */
	public boolean isAPFAuthorised(IZosDataset dataset, IZosImage image) throws ZosDatasetException;

	/**
	 * Instantiate a new {@link IZosVSAMDataset} object with the given name. The
	 * object returned is a 'blank-slate' and will require configuring before it
	 * can be defined or stored. See {@link #newESDS(String)},
	 * {@link #newKSDS(String)} or {@link #newRRDS(String)} for pre-configured
	 * VSAM objects.
	 * 
	 * @param dsName
	 * @return
	 */
	public IZosVSAMDataset newVSAMDataset(String dsName, IZosImage image);

	/**
	 * Instantiate a new {@link IZosVSAMDataset} object, pre-configured with
	 * options appropriate for a KSDS.
	 * 
	 * @param dsName
	 * @return
	 */
	public IZosVSAMDataset newKSDS(String dsName, IZosImage image);

	/**
	 * Instantiate a new {@link IZosVSAMDataset} object, pre-configured with
	 * options appropriate for a ESDS.
	 * 
	 * @param dsName
	 * @return
	 */
	public IZosVSAMDataset newESDS(String dsName, IZosImage image);

	/**
	 * Instantiate a new {@link IZosVSAMDataset} object, pre-configured with
	 * options appropriate for a RRDS.
	 * 
	 * @param dsName
	 * @return
	 */
	public IZosVSAMDataset newRRDS(String dsName, IZosImage image);

	/**
	 * Define an {@link IZosVSAMDataset} on the given zOS image.
	 * 
	 * @param vsam
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void define(IZosVSAMDataset vsam, IZosImage image) throws ZosDatasetException;

	/**
	 * Store an {@link IZosVSAMDataset} as a vsam on the given zOS image. The content
	 * stored is that added to the vsam using the
	 * {@link IZosVSAMDataset#setContent(String)} or
	 * {@link IZosVSAMDataset#appendContent(String)} methods.
	 * 
	 * @param vsam
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void store(IZosVSAMDataset vsam, IZosImage image) throws ZosDatasetException;

	/**
	 * Delete the VSAM corresponding to an {@link IZosVSAMDataset} from the given
	 * zOS image.
	 * 
	 * @param vsam
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void delete(IZosVSAMDataset vsam, IZosImage image) throws ZosDatasetException;

	/**
	 * Return true if the given {@link IZosVSAMDataset} exists on the given zOS image.
	 * 
	 * @param vsam
	 * @param image
	 * @return
	 * @throws ZosDatasetException
	 */
	public boolean exists(IZosVSAMDataset vsam, IZosImage image) throws ZosDatasetException;

	/**
	 * This method retrieves an artifact from the 'resources' folder in a jat
	 * bundle (test or infrastructure), performs substitutions using the '++'
	 * skeleton processor to generate the content which it will then store as a
	 * UNIX file using the transfer method appropriate to the filetype. See the
	 * description of the arguments for more information.
	 * 
	 * @param resourcePath
	 *            - the path to the resource in the bundle, specified relative
	 *            to 'resources'
	 * @param unixPath
	 *            - the absolute path to the target file in UNIX (need not exist)
	 * @param fileType
	 *            - {@link IZosUNIXFile#TEXT} or {@link IZosUNIXFile#BIN}
	 * @param image
	 *            - the zOS image for the target file
	 * @param substitutionParameters
	 *            - a map of key value pairs to be substituted into the stored
	 *            content
	 * @param owningClass
	 *            - any class in the bundle containing the resource to store.
	 *            'this.getClass()' is generally the safest way to specify.
	 * @return
	 * @throws ZosUNIXFileException 
	 */
	public IZosUNIXFile storeResourcesFile(String resourcePath, String unixPath,
			int fileType, IZosImage image,
			Map<String, Object> substitutionParameters, Class<?> owningClass)
			throws ZosUNIXFileException;

	/**
	 * This method retrieves an artifact from the 'resources' folder in a jat
	 * bundle (test or infrastructure), performs substitutions using the '++'
	 * skeleton processor to generate the content which it will then store as a
	 * dataset using the transfer method appropriate to the filetype. See the
	 * description of the arguments for more information.
	 * 
	 * @param resourcePath
	 *            - the path to the resource in the bundle, specified relative
	 *            to 'resources'
	 * @param dsName
	 *            - the name of the target dataset (need not exist)
	 * @param fileType
	 *            - {@link IZosDataset#TEXT} or {@link IZosDataset#BIN}
	 * @param image
	 *            - the zOS image for the target dataset
	 * @param substitutionParameters
	 *            - a map of key value pairs to be substituted into the stored
	 *            content
	 * @param owningClass
	 *            - any class in the bundle containing the resource to store.
	 *            'this.getClass()' is generally the safest way to specify.
	 * @return
	 * @throws ZosDatasetException
	 */
	public IZosDataset storeResourcesDataset(String resourcePath, String dsName,
			int fileType, IZosImage image,
			Map<String, Object> substitutionParameters, Class<?> owningClass)
			throws ZosDatasetException;

	/**
	 * This method retrieves all contents of a given directory from the
	 * 'resources' folder in a jat bundle (test or infrastructure). It will
	 * perform substitutions using the '++' skeleton processor on each file,
	 * then write those files to a tar archive, re-encoding as necessary. The
	 * archive is then transferred to UNIX and extracted in the directory
	 * requested. <br />
	 * <br />
	 * N.B. Contents are taken from the resourcePath directory, and put into the
	 * unixPath directory. So if resourcePath = "bundle/dir/" and unixPath =
	 * "/unix/target/", then "/resources/bundle/dir/file.txt" will end up at
	 * "/unix/target/file.txt".
	 * 
	 * @param resourcePath
	 *            - the path to the directory in the bundle, specified relative
	 *            to 'resources'
	 * @param unixPath
	 *            - the name of the target directory (need not exist)
	 * @param fileType
	 *            - {@link IZosUNIXFile#TEXT} or {@link IZosUNIXFile#BIN}
	 * @param image
	 *            - the zOS image for the target directory
	 * @param substitutionParameters
	 *            - a map of key value pairs to be substituted into the stored
	 *            content
	 * @param owningClass
	 *            - any class in the bundle containing the resource to store.
	 *            'this.getClass()' is generally the safest way to specify.
	 * @return
	 * @throws ZosUNIXFileException 
	 */
	public IZosUNIXFile storeResourcesDirectory(String resourcePath,
			String unixPath, int fileType, IZosImage image,
			Map<String, Object> substitutionParameters, Class<?> owningClass)
			throws ZosUNIXFileException;

	/**
	 * This method retrieves all contents of a given directory from the
	 * 'resources' folder in a jat bundle (test or infrastructure). It will
	 * perform substitutions using the '++' skeleton processor on each file,
	 * then write those files to a tar archive, re-encoding as necessary. The
	 * archive is then transferred to UNIX, extracted and each file is copied as
	 * a member to the requested PDS (which will be created if it does not
	 * exist). <br />
	 * <br />
	 * N.B. All files are copied from the resource directory and all
	 * sub-directories. The files will be upper-cased, and any file extensions
	 * stripped to generate the names of the PDS members
	 * 
	 * @param resourcePath
	 *            - the path to the directory in the bundle, specified relative
	 *            to 'resources'
	 * @param dsName
	 *            - the name of the target dataset (need not exist)
	 * @param pdsType
	 *            - {@link DSType#LIBRARY} or {@link DSType#PDS}
	 * @param fileType
	 *            - {@link IZosDataset#TEXT} or {@link IZosDataset#BIN}
	 * @param image
	 *            - the zOS image for the target PDS
	 * @param substitutionParameters
	 *            - a map of key value pairs to be substituted into the stored
	 *            content
	 * @param owningClass
	 *            - any class in the bundle containing the resource to store.
	 *            'this.getClass()' is generally the safest way to specify.
	 * @return
	 * @throws ZosDatasetException
	 */
	public IZosDataset storeResourcesPDS(String resourcePath, String dsName,
			DSType pdsType, int fileType, IZosImage image,
			Map<String, Object> substitutionParameters, Class<?> owningClass)
			throws ZosDatasetException;

	/**
	 * Store the content of a UNIX file with the test output.
	 * 
	 * @param file
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void storeFileToTestOutput(IZosUNIXFile file, IZosImage image)
			throws ZosDatasetException;

	/**
	 * Store the content of a Dataset with the test output.
	 * 
	 * @param dataset
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void storeDatasetToTestOutput(IZosDataset dataset, IZosImage image)
			throws ZosDatasetException;

	/**
	 * Delete all datasets (including VSAM datasets) with the given HLQ from the
	 * given zOS image
	 * 
	 * @param prefix
	 * @param image
	 * @throws ZosDatasetException
	 */
	public void deleteDatasetsByPrefix(String prefix, IZosImage image) throws ZosDatasetException;
}
