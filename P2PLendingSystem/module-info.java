/**
 * 
 */
/**
 * 
 */
module P2PLending {
	requires java.desktop;
	requires java.sql;              // Required for JDBC
    exports dao;                    // Export your DAO classes if other modules use them
    exports dsa_modules;            // Export DSA modules if needed
}