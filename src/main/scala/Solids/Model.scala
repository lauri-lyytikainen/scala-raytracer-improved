package Solids

import RayMath.{Ray, RotationMatrix3D, Vector3D}
import Rendering.Material

import scala.collection.mutable
import scala.io.Source
import scala.io.BufferedSource
import scala.collection.mutable.Buffer

/**
 * A model is a collection of triangles that make up a 3D object
 * @param fileName The name of the obj file to load the model from
 * @param position The position of the model
 * @param rm The rotation matrix of the model
 * @param size The size of the model
 * @param material The material of the model
 */
class Model(fileName: String, position: Vector3D,  rm: RotationMatrix3D, size: Double, material: Material) extends Solid(position, material):

  this.rotationMatrix = rm

  private val triangles: mutable.Buffer[Triangle] = mutable.Buffer[Triangle]()

  private val boundingSphere: Sphere = new Sphere(position, 0, material)

  val file: BufferedSource = Source.fromResource(fileName)
  if file.isEmpty then
    throw new Exception("File not found")
  else
    //load the file
    val vertices = mutable.Buffer[Vector3D]()
    var longestDistance = 0.0
    for (line <- file.getLines()) do
      //parse the line
      val splitLine = line.split(" ")
      if splitLine(0) == "v" then
        val x = splitLine(1).toDouble
        val y = splitLine(2).toDouble
        val z = splitLine(3).toDouble

        val v = Vector3D(x, y, z)
        val d = v.distance(Vector3D(0,0,0))
        if d > longestDistance then
          longestDistance = d
        //add a vertex
        vertices.append(v)
      else if splitLine(0) == "f" then
        //add a triangle

        val v1 = vertices(splitLine(1).toInt - 1) * size
        val v2 = vertices(splitLine(2).toInt - 1) * size
        val v3 = vertices(splitLine(3).toInt - 1) * size

        triangles.append(
          Triangle(
            rotationMatrix.rotatePoint(v1, Vector3D(0,0,0)) + position,
            rotationMatrix.rotatePoint(v2, Vector3D(0,0,0)) + position,
            rotationMatrix.rotatePoint(v3, Vector3D(0,0,0)) + position,
            material)
        )
    boundingSphere.radius = longestDistance * size

  override def getTextureColor(point: Vector3D): Vector3D =
    this.material.color

  override def calculateIntersection(ray: Ray): Option[Vector3D] =
    var closestHit: Option[Vector3D] = None
    for (triangle <- triangles) do
      val hit = triangle.calculateIntersection(ray)
      if hit.isDefined then
        if closestHit.isDefined then
          if hit.get.distance(ray.origin) < closestHit.get.distance(ray.origin) then
            closestHit = hit
        else
          closestHit = hit

    closestHit

  override def normalAt(point: Vector3D): Vector3D =
    //check the distance to each triangle
    //return the normal of the closest triangle
    var closestNormal: Option[Vector3D] = None
    var closestDistance = Double.MaxValue
    for (triangle <- triangles) do
      val d = triangle.distanceToPoint(point)
      if d < closestDistance then
        closestDistance = d
        closestNormal = Some(triangle.normalAt(point))

    closestNormal.get


