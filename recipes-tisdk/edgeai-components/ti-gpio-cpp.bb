SUMMARY = "TI GPIO cpp"
DESCRIPTION = "A Linux based CPP library for TI GPIO RPi header enabled platforms"
HOMEPAGE = "https://github.com/TexasInstruments/ti-gpio-cpp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.txt;md5=4a6102d7daa29b70c1abe088c13a0cde"

SRC_URI = "git://github.com/TexasInstruments/ti-gpio-cpp.git;protocol=https;branch=master"
SRCREV = "f0c33adfc65f87d4a6ec54ab04415eadc58ca4f1"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm"

EXTRA_OECMAKE = "-DCMAKE_SKIP_RPATH=TRUE"

PACKAGES += "${PN}-source"
FILES:${PN}-source += "/opt/"

inherit cmake

do_install:append() {
    CP_ARGS="-Prf --preserve=mode,timestamps --no-preserve=ownership"

    mkdir -p ${D}/opt/ti-gpio-cpp
    cp ${CP_ARGS} ${S}/* ${D}/opt/ti-gpio-cpp
    rm -rf ${D}/opt/ti-gpio-cpp/lib
    rm -rf ${D}/usr/cmake
}

PR:append = "_edgeai_1"
