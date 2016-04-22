package com.erban.imageloader;

public interface IHomeDataParser<T> {

    public T parse(String json) throws HomePageDataParseException;

    public static class HomePageDataParseException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 7916661169324056286L;

        public HomePageDataParseException(String message) {
            super(message);
        }
    }
}
