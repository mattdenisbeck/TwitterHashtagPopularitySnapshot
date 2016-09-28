package edu.nku.csc540.hashtag.stream;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;



public class PopularityDatapointProcessorFactory implements IRecordProcessorFactory { 
    /**
    * Constructor.
    */
    public PopularityDatapointProcessorFactory() {
        super();
    }
    /**
    * {@inheritDoc}
    */
    @Override
    public IRecordProcessor createProcessor() {
        return new PopularityDatapointProcessor();
    }
} 