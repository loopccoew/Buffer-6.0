import os # The os module is used for handling file paths and directories.

def reverse_bits(byte):
    """Reverse the bits of a single byte."""
    result = 0
    for _ in range(8): # Takes a single byte (8 bits, values 0–255).
        result = (result << 1) | (byte & 1) # calculations
        byte >>= 1
    return result

def decrypt_file(encrypted_file_path, decrypted_file_path):
    """Decrypt a file by reversing bits of each byte."""
    # Create the output folder if it doesn't exist
    os.makedirs(os.path.dirname(decrypted_file_path), exist_ok=True)#Ensures the folder (like decrypted_evidence/) exists.

    try:
        with open(encrypted_file_path, 'rb') as enc_file, open(decrypted_file_path, 'wb') as dec_file:
            while True:
                byte = enc_file.read(1)
                if not byte:
                    break
                byte_value = byte[0]  # Convert bytes to int
                reversed_byte = reverse_bits(byte_value)  # Reverse its bits
                dec_file.write(bytes([reversed_byte]))  # Write it back as a single byte

        print(f"Decryption successful! Decrypted file saved at:\n{decrypted_file_path}")#this decrypted file is stored in path folder
    except Exception as e:
        print(f"An error occurred during decryption: {e}")

if __name__ == "__main__":
    # -------- SET FILE PATHS HERE -----------
    
    encrypted_file = r'C:\Users\aryad\Downloads\Buffer-6.0_SecureHer-git\Buffer-6.0_SecureHer-main\CodeCrusaders 113-WomenSafety-SecureHer\encrypted_evidence\MM_report.docx.enc'#encrypted file path
    decrypted_file = r'C:\Users\aryad\Downloads\Buffer-6.0_SecureHer-git\Buffer-6.0_SecureHer-main\CodeCrusaders 113-WomenSafety-SecureHer\decrypted_evidence\MM_report.docx'#decrypted file path

    # ----------------------------------------------
    decrypt_file(encrypted_file, decrypted_file)
