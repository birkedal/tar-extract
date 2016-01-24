import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erlend on 24.01.16.
 */
public class UntarTest {

    public static void main(String[] args) {
        new UntarTest();
    }

    public UntarTest() {
        try {

            File targetDir = new File("out");
            // Cleaning up previous test runs
            if(targetDir.exists()) {
                targetDir.delete();
            }

            File tarFile = new File(this.getClass().getResource("test.tar").getFile());
            TarArchiveInputStream tarStream = new TarArchiveInputStream(new FileInputStream(tarFile));

            TarArchiveEntry entry;
            while ((entry = tarStream.getNextTarEntry()) != null) {
                if (!entry.isDirectory()) {
                    File curFile = new File(targetDir, entry.getName());
                    File parent = curFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    IOUtils.copy(tarStream, new FileOutputStream(curFile));

                }
            }
            tarStream.close();

            //Let's see what we got
            echoRecuse(targetDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void echoRecuse(final File directory) throws IOException {
        for(File file :directory.listFiles()) {
            if(file.isDirectory()) {
                echoRecuse(file);
            } else {
                Files.readAllLines(file.toPath()).stream().forEach(System.out::print);
            }
        }
    }
}
