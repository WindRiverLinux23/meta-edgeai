SUMMARY = "EdgeAI Studio Agent"
DESCRIPTION = "EdgeAI Device Agent runs on TI devices to enable EdgeAI Studio tool"
HOMEPAGE = "https://github.com/TexasInstruments/edgeai-studio-agent"

LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=3677661f72cd03c7b3c0a35e5fb23e8d"

PV = "${SRCPV}"
SRC_URI = "git://github.com/TexasInstruments/edgeai-studio-agent.git;branch=develop;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RDEPENDS:${PN} += "edgeai-gst-apps-source bash python3-core python3-aiofiles python3-websocket-client python3-uvicorn python3-fastapi python3-python-multipart python3-websockets python3-psutil cors express nodejs"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm"

FILES:${PN} += "/opt"

do_install() {
    CP_ARGS="-Prf --preserve=mode,timestamps --no-preserve=ownership"

    mkdir -p ${D}/opt/edgeai-studio-agent
    cp ${CP_ARGS} ${WORKDIR}/git/* ${D}/opt/edgeai-studio-agent
}

PR:append = "_edgeai_0"
