netty4-flash-policy-file-server
===============================

A simple ByteToMessageDecoder used to decode the flash policy file request. A complete WebSocket Server code is included. The decoder is coded such that it works on the same HTTP server you are using for serving HTTP requests.
Just add an instance of ByteToPolicyDecoder into the ChannelPipeline
