package cbit.vcell.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import cbit.vcell.util.AmplistorUtils.AmplistorCredential;

public class UnarchiveAmplistor {
	/**
	 * file in $HOME to stash password
	 */
	private static final String PASSWORD_FILE = ".unarchivepw";
	
	public static void main(String[] args) {
		try{
			String amplistorCredentialCleartextPassword = null;  //can set directly here, or
			if (amplistorCredentialCleartextPassword == null) {
				amplistorCredentialCleartextPassword = readFromHomeDirectory();
				
			}
			
			if (amplistorCredentialCleartextPassword == null) {
				System.err.println("need password");
				return;
			}

			// /share/apps/vcell2/users/boris/SimID_87497385_0__0.simtask.xml
			// /share/apps/vcell2/users/boris/SimID_87497385_0_.fvinput
			// /share/apps/vcell2/users/boris/SimID_87497385_0_.smoldynInput
			// /share/apps/vcell2/users/boris/SimID_87497385_0_.smoldynOutput
			// /share/apps/vcell2/users/boris/SimID_87497385_0_.tid
			// /share/apps/vcell2/users/boris/SimID_87497385_0_.vcg
			String[] fileNames = new String[] {
					"SimID_87497385_0__0.simtask.xml",
					"SimID_87497385_0_.fvinput",
					"SimID_87497385_0_.smoldynInput",
					"SimID_87497385_0_.smoldynOutput",
					"SimID_87497385_0_.tid",
					"SimID_87497385_0_.vcg"
			};
			String amplistorCredentialUser = "vcell";
			AmplistorCredential amplistorCredential = new AmplistorCredential(amplistorCredentialUser, amplistorCredentialCleartextPassword);
			for(String fileName:fileNames){
				//not sure where isolon is anymore
				File destinationFile = new File("\\\\cfs02\\raid\\vcell\\users\\boris\\"+fileName);
				AmplistorUtils.getObjectDataPutInFile(
						AmplistorUtils.DEFAULT_AMPLI_SERVICE_VCELL_URL+"/boris/"+fileName,amplistorCredential, destinationFile);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * read from {@value #PASSWORD_FILE} in home directory
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static String readFromHomeDirectory( ) throws FileNotFoundException, IOException {
		String home = System.getProperty("user.home");
		if (home != null) {
			File pfile = new File (home,PASSWORD_FILE);
			if (pfile.canRead()) {
				return IOUtils.toString(new FileReader(pfile)).trim();
			}
		}
		return null;
	}

}
