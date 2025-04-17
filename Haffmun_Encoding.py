import heapq
from collections import defaultdict

# Helper function to create frequency dictionary of characters in the input text
def create_frequency_dict(text):
    frequency = defaultdict(int)
    for char in text:
        frequency[char] += 1
    return frequency

# Helper function to build the Huffman tree
def build_huffman_tree(frequency):
    heap = [[weight, [char, ""]] for char, weight in frequency.items()]
    heapq.heapify(heap)
    
    while len(heap) > 1:
        lo = heapq.heappop(heap)
        hi = heapq.heappop(heap)
        for pair in lo[1:]:
            pair[1] = '0' + pair[1]
        for pair in hi[1:]:
            pair[1] = '1' + pair[1]
        heapq.heappush(heap, [lo[0] + hi[0]] + lo[1:] + hi[1:])
    
    return heap[0]

# Helper function to create the Huffman encoding map
def create_huffman_encoding(tree):
    huffman_encoding = {}
    for pair in tree[1:]:
        huffman_encoding[pair[0]] = pair[1]
    return huffman_encoding

# Helper function to encode the text using the Huffman encoding map
def encode_text(text, encoding_map):
    return ''.join(encoding_map[char] for char in text)

# Helper function to decode the encoded text using the Huffman tree
def decode_text(encoded_text, tree):
    reverse_encoding_map = {v: k for k, v in tree[1:]}
    decoded_text = []
    code = ""
    for bit in encoded_text:
        code += bit
        if code in reverse_encoding_map:
            decoded_text.append(reverse_encoding_map[code])
            code = ""
    return ''.join(decoded_text)

# Example usage for compression and decompression
def huffman_compress_decompress(text):
    # Step 1: Create a frequency dictionary from the text
    frequency = create_frequency_dict(text)
    
    # Step 2: Build the Huffman tree
    huffman_tree = build_huffman_tree(frequency)
    
    # Step 3: Create the Huffman encoding map
    encoding_map = create_huffman_encoding(huffman_tree)
    
    # Step 4: Encode the text using the encoding map
    encoded_text = encode_text(text, encoding_map)
    
    # Step 5: Decode the encoded text using the Huffman tree
    decoded_text = decode_text(encoded_text, huffman_tree)
    
    # Return encoded and decoded text
    return encoded_text, decoded_text
