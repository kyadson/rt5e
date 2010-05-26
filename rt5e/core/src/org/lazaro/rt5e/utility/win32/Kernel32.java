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
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.nio.Buffer;

/**
 * Definition (incomplete) of <code>kernel32.dll</code>.
 */
public interface Kernel32 extends W32API {
    Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32",
            Kernel32.class, DEFAULT_OPTIONS);

    /**
     * This structure is non-trivial since it is a pattern stamped into a large
     * block of result memory rather than something that stands alone or is used
     * for input.
     */
    class FILE_NOTIFY_INFORMATION extends Structure {
        public int Action;
        // filename is not nul-terminated, so we can't use a String/WString
        public char[] FileName = new char[1];
        public int FileNameLength;
        public int NextEntryOffset;

        private FILE_NOTIFY_INFORMATION() {
        }

        public FILE_NOTIFY_INFORMATION(int size) {
            if (size < size())
                throw new IllegalArgumentException("Size must greater than "
                        + size() + ", requested " + size);
            allocateMemory(size);
        }

        /**
         * WARNING: this filename may be either the short or long form of the
         * filename.
         */
        public String getFilename() {
            return new String(FileName, 0, FileNameLength / 2);
        }

        public FILE_NOTIFY_INFORMATION next() {
            if (NextEntryOffset == 0)
                return null;
            FILE_NOTIFY_INFORMATION next = new FILE_NOTIFY_INFORMATION();
            next.useMemory(getPointer(), NextEntryOffset);
            next.read();
            return next;
        }

        @Override
        public void read() {
            // avoid reading filename until we know how long it is
            FileName = new char[0];
            super.read();
            FileName = getPointer().getCharArray(12, FileNameLength / 2);
        }
    }

    class OVERLAPPED extends Structure {
        public HANDLE hEvent;
        public ULONG_PTR Internal;
        public ULONG_PTR InternalHigh;
        public int Offset;
        public int OffsetHigh;
    }

    // TODO: figure out how OVERLAPPED is used and apply an appropriate mapping

    interface OVERLAPPED_COMPLETION_ROUTINE extends StdCallLibrary.StdCallCallback {
        void callback(int errorCode, int nBytesTransferred,
                      OVERLAPPED overlapped);
    }

    class SECURITY_ATTRIBUTES extends Structure {
        public boolean bInheritHandle;
        public Pointer lpSecurityDescriptor;
        public int nLength = size();
    }

    class SYSTEMTIME extends Structure {
        public short wDay;
        public short wDayOfWeek;
        public short wHour;
        public short wMilliseconds;
        public short wMinute;
        public short wMonth;
        public short wSecond;
        public short wYear;
    }

    int ABOVE_NORMAL_PRIORITY_CLASS = 0x00008000;
    int BELOW_NORMAL_PRIORITY_CLASS = 0x00004000;
    int CREATE_ALWAYS = 2;
    int CREATE_NEW = 1;
    long DELETE = 0x00010000L;
    int DRIVE_CDROM = 5;
    int DRIVE_FIXED = 3;
    int DRIVE_NO_ROOT_DIR = 1;
    int DRIVE_RAMDISK = 6;
    int DRIVE_REMOTE = 4;
    int DRIVE_REMOVABLE = 2;
    int DRIVE_UNKNOWN = 0;
    int FILE_ACTION_ADDED = 1;
    int FILE_ACTION_MODIFIED = 3;
    int FILE_ACTION_REMOVED = 2;
    int FILE_ACTION_RENAMED_NEW_NAME = 5;
    int FILE_ACTION_RENAMED_OLD_NAME = 4;
    int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;
    int FILE_ATTRIBUTE_COMPRESSED = 0x00000800;
    int FILE_ATTRIBUTE_DEVICE = 0x00000040;
    int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
    int FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;
    int FILE_ATTRIBUTE_HIDDEN = 0x00000002;
    int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;
    int FILE_ATTRIBUTE_OFFLINE = 0x00001000;
    int FILE_ATTRIBUTE_READONLY = 0x00000001;
    int FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;
    int FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;
    int FILE_ATTRIBUTE_SYSTEM = 0x00000004;
    int FILE_ATTRIBUTE_TEMPORARY = 0x00000100;
    int FILE_FLAG_BACKUP_SEMANTICS = 0x02000000;
    int FILE_FLAG_DELETE_ON_CLOSE = 0x04000000;
    int FILE_FLAG_NO_BUFFERING = 0x20000000;
    int FILE_FLAG_OPEN_NO_RECALL = 0x00100000;
    int FILE_FLAG_OPEN_REPARSE_POINT = 0x00200000;
    int FILE_FLAG_OVERLAPPED = 0x40000000;
    int FILE_FLAG_POSIX_SEMANTICS = 0x01000000;
    int FILE_FLAG_RANDOM_ACCESS = 0x10000000;
    int FILE_FLAG_SEQUENTIAL_SCAN = 0x08000000;
    int FILE_FLAG_WRITE_THROUGH = 0x80000000;
    int FILE_LIST_DIRECTORY = 0x00000001;
    int FILE_NOTIFY_CHANGE_ATTRIBUTES = 4;
    int FILE_NOTIFY_CHANGE_CREATION = 64;
    int FILE_NOTIFY_CHANGE_DIR_NAME = 2;
    int FILE_NOTIFY_CHANGE_EA = 128;
    int FILE_NOTIFY_CHANGE_FILE_NAME = 1;
    int FILE_NOTIFY_CHANGE_LAST_ACCESS = 32;
    int FILE_NOTIFY_CHANGE_LAST_WRITE = 16;
    int FILE_NOTIFY_CHANGE_NAME = 3;
    int FILE_NOTIFY_CHANGE_SECURITY = 256;
    int FILE_NOTIFY_CHANGE_SIZE = 8;
    int FILE_NOTIFY_CHANGE_STREAM_NAME = 512;
    int FILE_NOTIFY_CHANGE_STREAM_SIZE = 1024;
    int FILE_NOTIFY_CHANGE_STREAM_WRITE = 2048;
    int FILE_SHARE_DELETE = 4;
    int FILE_SHARE_READ = 1;
    int FILE_SHARE_WRITE = 2;
    int FORMAT_MESSAGE_ALLOCATE_BUFFER = 0x0100;
    int FORMAT_MESSAGE_FROM_SYSTEM = 0x1000;
    int FORMAT_MESSAGE_IGNORE_INSERTS = 0x200;
    int GENERIC_WRITE = 0x40000000;
    int HIGH_PRIORITY_CLASS = 0x00000080;
    int IDLE_PRIORITY_CLASS = 0x00000040;
    int INFINITE = 0xFFFFFFFF;
    int NORMAL_PRIORITY_CLASS = 0x00000020;
    int OPEN_ALWAYS = 4;
    int OPEN_EXISTING = 3;
    int PROCESS_CREATE_PROCESS = 0x0080;
    int PROCESS_CREATE_THREAD = 0x0002;
    int PROCESS_DUP_HANDLE = 0x0040;
    int PROCESS_MODE_BACKGROUND_BEGIN = 0x00100000;
    int PROCESS_MODE_BACKGROUND_END = 0x00200000;
    int PROCESS_QUERY_INFORMATION = 0x0400;
    int PROCESS_QUERY_LIMITED_INFORMATION = 0x1000;
    int PROCESS_SET_INFORMATION = 0x0200;
    int PROCESS_SET_QUOTA = 0x0100;
    int PROCESS_SUSPEND_RESUME = 0x0800;
    int PROCESS_TERMINATE = 0x0001;
    int PROCESS_VM_OPERATION = 0x0008;
    int PROCESS_VM_READ = 0x0010;
    int PROCESS_VM_WRITE = 0x0020;
    long READ_CONTROL = 0x00020000L;
    int REALTIME_PRIORITY_CLASS = 0x00000100;
    long SYNCHRONIZE = 0x00100000L;
    long WRITE_DAC = 0x00040000L;
    long WRITE_OWNER = 0x00080000L;
    long STANDARD_RIGHTS_ALL = DELETE | READ_CONTROL | WRITE_DAC | WRITE_OWNER
            | SYNCHRONIZE;
    int STD_ERROR_HANDLE = -11;
    int STD_INPUT_HANDLE = -10;
    int STD_OUTPUT_HANDLE = -11;
    int TRUNCATE_EXISTING = 5;

    boolean CloseHandle(HANDLE hObject);

    boolean CreateDirectory();

    HANDLE CreateFile(String lpFileName, int dwDesiredAccess, int dwShareMode,
                      SECURITY_ATTRIBUTES lpSecurityAttributes,
                      int dwCreationDisposition, int dwFlagsAndAttributes,
                      HANDLE hTemplateFile);

    HANDLE CreateIoCompletionPort(HANDLE FileHandle,
                                  HANDLE ExistingCompletionPort, Pointer CompletionKey,
                                  int NumberOfConcurrentThreads);

    boolean DuplicateHandle(HANDLE hSourceProcessHandle, HANDLE hSourceHandle,
                            HANDLE hTargetProcessHandle, HANDLEByReference lpTargetHandle,
                            int dwDesiredAccess, boolean bInheritHandle, int dwOptions);

    int FormatMessage(int dwFlags, Pointer lpSource, int dwMessageId,
                      int dwLanguageId, Buffer lpBuffer, int nSize, Pointer va_list);

    int FormatMessage(int dwFlags, Pointer lpSource, int dwMessageId,
                      int dwLanguageId, PointerByReference lpBuffer, int nSize,
                      Pointer va_list);

    HANDLE GetCurrentProcess();

    int GetCurrentProcessId();

    HANDLE GetCurrentThread();

    int GetCurrentThreadId();

    int GetDriveType(String rootPathName);

    int GetLastError();

    HMODULE GetModuleHandle(String name);

    int GetProcessId(HANDLE process);

    int GetProcessVersion(int processId);

    boolean GetQueuedCompletionStatus(HANDLE CompletionPort,
                                      IntByReference lpNumberOfBytes, ByReference lpCompletionKey,
                                      PointerByReference lpOverlapped, int dwMilliseconds);

    /**
     * ASCII version. Use {@link Native#toString(byte[])} to obtain the short
     * path from the <code>byte</code> array. Use only if
     * <code>w32.ascii==true</code>.
     */
    int GetShortPathName(String lpszLongPath, byte[] lpdzShortPath,
                         int cchBuffer);

    /**
     * Unicode version (the default). Use {@link Native#toString(char[])} to
     * obtain the short path from the <code>char</code> array.
     */
    int GetShortPathName(String lpszLongPath, char[] lpdzShortPath,
                         int cchBuffer);

    HANDLE GetStdHandle(int nStdHandle);

    void GetSystemTime(SYSTEMTIME result);

    Pointer GlobalFree(Pointer hGlobal);

    Pointer LocalFree(Pointer hLocal);

    HANDLE OpenProcess(int dwDesiredAccess, int bInheritHandle, int dwProcessId);

    boolean PostQueuedCompletionStatus(HANDLE CompletionPort,
                                       int dwNumberOfBytesTransferred, Pointer dwCompletionKey,
                                       OVERLAPPED lpOverlapped);

    /**
     * NOTE: only exists in unicode form (W suffix). Define this method
     * explicitly with the W suffix to avoid inadvertent calls in ASCII mode.
     */
    boolean ReadDirectoryChangesW(HANDLE directory,
                                  FILE_NOTIFY_INFORMATION info, int length, boolean watchSubtree,
                                  int notifyFilter, IntByReference bytesReturned,
                                  OVERLAPPED overlapped,
                                  OVERLAPPED_COMPLETION_ROUTINE completionRoutine);

    int SetConsoleTextAttribute(HANDLE hConsoleOutput, short wAttributes);

    int SetConsoleTitle(String lpConsoleTitle);

    void SetLastError(int dwErrCode);

    boolean SetPriorityClass(HANDLE hProcess, int dwPriorityClass);

    int WaitForSingleObject(HANDLE hHandle, int dwMilliseconds);
}
