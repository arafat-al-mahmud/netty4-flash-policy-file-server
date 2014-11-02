

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GenericFutureListener;


public class ByteToPolicyDecoder extends ByteToMessageDecoder{

	
	private int firstByte=0;
	private int secondByte=0;
	private boolean ltFound=false;
	private boolean pFound=false;
	int ltIndex;
	
	private static final String XML = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>";
	private ByteBuf policyResponse = Unpooled.copiedBuffer(XML, CharsetUtil.UTF_8);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		

						
			
			if(buffer.readableBytes()>0)
			{
				if(!ltFound)
				{
					firstByte=buffer.getUnsignedByte(buffer.readerIndex());
				
				}
							
				if(firstByte=='<')
				{
					ltFound=true;
					ltIndex=buffer.readerIndex();
				}
				
				
				if(buffer.readableBytes()>1 && ltFound)
				{
					secondByte=buffer.getUnsignedByte(ltIndex+1);
					
					if(secondByte=='p')
					{
						pFound=true;
					}
				}
				
				if(ltFound && pFound)
				{			    
		            buffer.skipBytes(buffer.readableBytes());
		            removeAllPipelineHandlers(ctx.pipeline());
		            		            	            
					ctx.writeAndFlush(policyResponse).addListener(ChannelFutureListener.CLOSE);
				}
				else{
					out.add( buffer.readBytes(buffer.readableBytes()));
				}
				
			}		

	}
	
	private void removeAllPipelineHandlers(ChannelPipeline pipeline) {
        while (pipeline.first() != null) {
            pipeline.removeFirst();
        }
    }
	

}
