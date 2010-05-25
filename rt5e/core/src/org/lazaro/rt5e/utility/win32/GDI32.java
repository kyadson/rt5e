/* 
 * Copyright (c) 2007 Timothy Wall, All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.lazaro.rt5e.utility.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import org.lazaro.rt5e.utility.win32.User32.POINT;

import java.awt.*;

/**
 * Definition (incomplete) of <code>gdi32.dll</code>.
 */
public interface GDI32 extends W32API {
    GDI32 INSTANCE = (GDI32) Native.loadLibrary("gdi32", GDI32.class,
            DEFAULT_OPTIONS);

    class BITMAPINFO extends Structure {
        // RGBQUAD:
        // byte rgbBlue;
        // byte rgbGreen;
        // byte rgbRed;
        // byte rgbReserved = 0;
        int[] bmiColors = new int[1];
        public BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();

        public BITMAPINFO() {
            this(1);
        }

        public BITMAPINFO(int size) {
            bmiColors = new int[size];
            allocateMemory();
        }
    }

    class BITMAPINFOHEADER extends Structure {
        public short biBitCount;
        public int biClrImportant;
        public int biClrUsed;
        public int biCompression;
        public int biHeight;
        public short biPlanes;
        public int biSize = size();
        public int biSizeImage;
        public int biWidth;
        public int biXPelsPerMeter;
        public int biYPelsPerMeter;
    }

    class DEVMODE extends Structure {
        // TODO Not complete.
    }

    class RECT extends Structure {
        public int bottom;
        public int left;
        public int right;
        public int top;

        public Rectangle toRectangle() {
            return new Rectangle(left, top, right - left, bottom - top);
        }

        @Override
        public String toString() {
            return "[(" + left + "," + top + ")(" + right + "," + bottom + ")]";
        }
    }

    class RGBQUAD extends Structure {
        public byte rgbBlue;
        public byte rgbGreen;
        public byte rgbRed;
        public byte rgbReserved = 0;
    }

    class RGNDATA extends Structure {
        public byte[] Buffer;
        public RGNDATAHEADER rdh;

        public RGNDATA(int bufferSize) {
            Buffer = new byte[bufferSize];
            allocateMemory();
        }
    }

    class RGNDATAHEADER extends Structure {
        public int dwSize = size();
        public int iType = RDH_RECTANGLES; // required
        public int nCount;
        public int nRgnSize;
        public RECT rcBound;
    }

    int ALTERNATE = 1;
    int BI_BITFIELDS = 3;
    int BI_JPEG = 4;
    int BI_PNG = 5;
    int BI_RGB = 0;

    int BI_RLE4 = 2;
    int BI_RLE8 = 1;
    int COMPLEXREGION = 3;
    int DIB_PAL_COLORS = 1;

    int DIB_RGB_COLORS = 0;

    int ERROR = 0;

    int NULLREGION = 1;
    int RDH_RECTANGLES = 1;

    int RGN_AND = 1;

    int RGN_COPY = 5;

    int RGN_DIFF = 4;

    int RGN_OR = 2;

    int RGN_XOR = 3;

    int SIMPLEREGION = 2;
    int WINDING = 2;

    int CombineRgn(HRGN hrgnDest, HRGN hrgnSrc1, HRGN hrgnSrc2,
                   int fnCombineMode);

    HBITMAP CreateCompatibleBitmap(HDC hDC, int width, int height);

    HDC CreateCompatibleDC(HDC hDC);

    HDC CreateDC(String lpszDriver, String lpszDevice, String lpszOutput,
                 DEVMODE.ByReference lpInitData);

    HBITMAP CreateDIBitmap(HDC hDC, BITMAPINFOHEADER lpbmih, int fdwInit,
                           Pointer lpbInit, BITMAPINFO lpbmi, int fuUsage);

    HBITMAP CreateDIBSection(HDC hDC, BITMAPINFO pbmi, int iUsage,
                             PointerByReference ppvBits, Pointer hSection, int dwOffset);

    HRGN CreatePolyPolygonRgn(POINT[] lppt, int[] lpPolyCounts, int nCount,
                              int fnPolyFillMode);

    HRGN CreateRectRgn(int nLeftRect, int nTopRect, int nRightRect,
                       int nBottomRect);

    HRGN CreateRoundRectRgn(int nLeftRect, int nTopRect, int nRightRect,
                            int nBottomRect, int nWidthEllipse, int nHeightEllipse);

    boolean DeleteDC(HDC hDC);

    boolean DeleteObject(HANDLE p);

    public HRGN ExtCreateRegion(Pointer lpXform, int nCount, RGNDATA lpRgnData);

    HANDLE SelectObject(HDC hDC, HANDLE hGDIObj);

    int SetPixel(HDC hDC, int x, int y, int crColor);

    boolean SetRectRgn(HRGN hrgn, int nLeftRect, int nTopRect, int nRightRect,
                       int nBottomRect);
}
