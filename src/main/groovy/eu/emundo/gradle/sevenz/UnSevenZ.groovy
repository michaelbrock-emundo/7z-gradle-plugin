package eu.emundo.gradle.sevenz

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * Gradle Task whic extracts a 7z archive.
 */
class UnSevenZ extends DefaultTask {

    @InputFile
    File sourceFile

    @OutputDirectory
    File outputDir

    @TaskAction
    void extract(IncrementalTaskInputs inputs) {
        SevenZFile sevenZFile = new SevenZFile(sourceFile)
        SevenZArchiveEntry entry
        while ((entry = sevenZFile.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue
            }
            File curfile = new File(outputDir, entry.getName())
            File parent = curfile.getParentFile()
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }

            final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(curfile));
            int currByte = 0;

            try {
                while( (currByte = sevenZFile.read()) != -1 ) {
                    out.write(currByte);
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

}
