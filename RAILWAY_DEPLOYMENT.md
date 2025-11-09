# Railway Deployment Guide

## Quick Railway Deployment

Railway.app provides automatic deployment from GitHub with zero configuration needed!

### Step 1: Push to GitHub

```bash
# Initialize git (if not already done)
git init
git add .
git commit -m "Initial commit: SRRC Calendar API"

# Add your GitHub repository
git remote add origin https://github.com/YOUR_USERNAME/srrc-calendar-api.git
git push -u origin main
```

### Step 2: Deploy to Railway

1. Visit [railway.app](https://railway.app)
2. Click "Start a New Project"
3. Select "Deploy from GitHub repo"
4. Choose `srrc-calendar-api` repository
5. Railway will automatically:
   - Detect the Dockerfile
   - Build the image
   - Deploy your API
   - Assign a public URL
   - Set the PORT environment variable

### Step 3: Verify Deployment

Railway will provide a URL like: `https://your-app.railway.app`

Test it:
```bash
# Health check
curl https://your-app.railway.app/actuator/health

# Get events
curl https://your-app.railway.app/api/v1/events
```

## Railway Configuration

### Health Check

Railway will automatically use:
- **Path**: `/actuator/health`
- **Expected Status**: 200

### Environment Variables

Railway automatically sets:
- `PORT` - The port your app should listen on

Optional variables you can add:
- `SPRING_PROFILES_ACTIVE=prod` - Use production configuration

### Restart Policy

Railway automatically restarts your service if it crashes.

## Monitoring in Railway

Railway Dashboard provides:
- **Build Logs**: See the Docker build process
- **Deploy Logs**: See application startup logs
- **Metrics**: CPU, Memory, Network usage
- **Health Status**: Green when healthy

## Updating Your Deployment

Simply push to GitHub:
```bash
git add .
git commit -m "Update API"
git push
```

Railway will automatically:
1. Detect the push
2. Build new Docker image
3. Deploy with zero downtime
4. Roll back automatically if health check fails

## Custom Domain

In Railway Dashboard:
1. Go to your service settings
2. Click "Settings" → "Domains"
3. Add your custom domain
4. Configure DNS records as shown

## Troubleshooting

### View Logs
```bash
# Install Railway CLI
npm i -g @railway/cli

# Login
railway login

# View logs
railway logs
```

### Common Issues

**Build fails:**
- Check build logs in Railway dashboard
- Verify Dockerfile is correct
- Ensure all dependencies are specified in build.gradle.kts

**Health check fails:**
- Verify `/actuator/health` returns 200
- Check application logs for startup errors
- Ensure PORT environment variable is used

**API not responding:**
- Check health status in Railway dashboard
- View deploy logs for errors
- Verify GitHub API is accessible from Railway

## Alternative: Railway CLI Deployment

```bash
# Install Railway CLI
npm i -g @railway/cli

# Login
railway login

# Link to project
railway link

# Deploy
railway up
```

## Cost Estimation

Railway offers:
- **Hobby Plan**: $5/month for hobby projects
- **Free Trial**: Available for testing
- **Usage-based pricing**: Pay for what you use

Your SRRC Calendar API should run comfortably on the Hobby plan.

## Production Checklist

Before going live:

- [ ] Push code to GitHub
- [ ] Deploy to Railway
- [ ] Test all endpoints with production URL
- [ ] Set up custom domain (optional)
- [ ] Configure monitoring/alerts
- [ ] Update CORS settings for your domain
- [ ] Test scheduled refresh is working
- [ ] Verify health checks are passing

## Support

- **Railway Docs**: https://docs.railway.app
- **Railway Discord**: https://discord.gg/railway
- **Railway Status**: https://status.railway.app

---

**Your API is Railway-ready! 🚂**

Just push to GitHub and deploy!
