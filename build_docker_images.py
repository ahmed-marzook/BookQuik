#!/usr/bin/env python3
"""
Script to build Docker images for BookQuik microservices.
This script builds the service JARs and creates Docker images for each service.
Windows compatible version.
"""

import os
import subprocess
import sys
import argparse
import platform
from datetime import datetime


# Configuration
SERVICES = {
    "inventory": {
        "path": "./inventory",
        "port": 8081,
        "jar_name": "inventory-0.0.1-SNAPSHOT.jar",
    },
    "booking": {
        "path": "./booking",
        "port": 8082,
        "jar_name": "booking-0.0.1-SNAPSHOT.jar",
    },
    "order": {
        "path": "./order",
        "port": 8083,
        "jar_name": "order-0.0.1-SNAPSHOT.jar",
    }
}

# Docker image naming
DOCKER_REGISTRY = "bookquik"
IMAGE_VERSION = datetime.now().strftime("%Y%m%d%H%M")

# Determine if we're on Windows
IS_WINDOWS = platform.system() == "Windows"


def run_command(command, cwd=None):
    """Run a shell command and print output."""
    print(f"Running: {command}")
    try:
        # Use shell=True only on Windows
        result = subprocess.run(
            command,
            shell=True,
            check=True,
            cwd=cwd,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print(result.stdout)
        return True
    except subprocess.CalledProcessError as e:
        print(f"Error executing command: {command}")
        print(f"Return code: {e.returncode}")
        print(f"Output: {e.stdout}")
        print(f"Error: {e.stderr}")
        return False


def build_service(service_name, skip_tests=False):
    """Build a service using Gradle and create a Docker image."""
    service_info = SERVICES.get(service_name)
    if not service_info:
        print(f"Error: Service '{service_name}' not found in configuration")
        return False

    service_path = service_info["path"]
    if not os.path.exists(service_path):
        print(f"Error: Service path '{service_path}' does not exist")
        return False

    print(f"\n=== Building {service_name} service ===")
    
    # Build with Gradle - use appropriate command for Windows or Unix
    if IS_WINDOWS:
        gradle_cmd = "gradlew.bat clean build"
    else:
        gradle_cmd = "./gradlew clean build"
        
    if skip_tests:
        gradle_cmd += " -x test"
    
    if not run_command(gradle_cmd, cwd=service_path):
        print(f"Failed to build {service_name} service")
        return False
    
    # Check if JAR was built
    jar_path = os.path.join(service_path, "build/libs", service_info["jar_name"])
    if not os.path.exists(jar_path):
        print(f"Error: JAR file not found at {jar_path}")
        return False
    
    # Build Docker image
    print(f"\n=== Building Docker image for {service_name} service ===")
    image_name = f"{DOCKER_REGISTRY}/{service_name}:latest"
    image_name_versioned = f"{DOCKER_REGISTRY}/{service_name}:{IMAGE_VERSION}"
    
    if not run_command(f"docker build -t {image_name} -t {image_name_versioned} .", cwd=service_path):
        print(f"Failed to build Docker image for {service_name}")
        return False
    
    print(f"Successfully built image: {image_name} and {image_name_versioned}")
    return True


def main():
    parser = argparse.ArgumentParser(description="Build BookQuik service Docker images")
    parser.add_argument("--services", nargs="+", choices=list(SERVICES.keys()) + ["all"], 
                      default="all", help="Services to build (default: all)")
    parser.add_argument("--skip-tests", action="store_true", help="Skip running tests during build")
    parser.add_argument("--push", action="store_true", help="Push images to Docker registry")
    
    args = parser.parse_args()
    
    services_to_build = list(SERVICES.keys()) if args.services == "all" else args.services
    
    # Build each service
    successful_builds = []
    failed_builds = []
    
    for service in services_to_build:
        if build_service(service, args.skip_tests):
            successful_builds.append(service)
        else:
            failed_builds.append(service)
    
    # Push images if requested
    if args.push and successful_builds:
        print("\n=== Pushing Docker images to registry ===")
        for service in successful_builds:
            image_name = f"{DOCKER_REGISTRY}/{service}:latest"
            image_name_versioned = f"{DOCKER_REGISTRY}/{service}:{IMAGE_VERSION}"
            
            if run_command(f"docker push {image_name}"):
                print(f"Successfully pushed {image_name}")
            
            if run_command(f"docker push {image_name_versioned}"):
                print(f"Successfully pushed {image_name_versioned}")
    
    # Print summary
    print("\n=== Build Summary ===")
    if successful_builds:
        print(f"Successfully built: {', '.join(successful_builds)}")
    if failed_builds:
        print(f"Failed to build: {', '.join(failed_builds)}")
        return 1
    
    print("\nAll services built successfully!")
    return 0


if __name__ == "__main__":
    sys.exit(main())