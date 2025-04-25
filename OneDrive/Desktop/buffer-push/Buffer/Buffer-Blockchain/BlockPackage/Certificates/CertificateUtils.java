package BlockPackage.Certificates;

import BlockPackage.DataStructure.*;



public class CertificateUtils {

    public static void searchByName(Blockchain<Certificate> chain, String name) {
        boolean found = false;
        for (Block<Certificate> block : chain.getChain()) {
            for (Certificate cert : block.getData()) {
                if (cert.getStudentName().equalsIgnoreCase(name)) {
                    System.out.println(cert);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No certificate found for student: " + name);
        }
    }

    public static void clearCertificateChain(Blockchain<Certificate> chain) {
        chain.getChain().clear();
        Block<Certificate> genesis = chain.createGenesisBlock(); // Use the method you already have
        chain.getChain().add(genesis);
        System.out.println("Certificate blockchain has been reset.");
    }

    public static Block<Certificate> getBlock(Blockchain<Certificate> chain, int index) {
        if (index >= 0 && index < chain.getChain().size()) {
            return chain.getChain().get(index);
        }
        throw new IndexOutOfBoundsException("Invalid block index.");
    }
}
